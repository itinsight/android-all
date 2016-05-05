package mobi.esys.server;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Children;
import com.google.api.services.drive.model.ChildList;
import com.google.api.services.drive.model.ChildReference;
import com.google.api.services.drive.model.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import mobi.esys.constants.UNLConsts;
import mobi.esys.data.GDFile;
import mobi.esys.fileworks.DirectoryWorks;
import mobi.esys.fileworks.FileWorks;
import mobi.esys.upnews_tune.UNLApp;

public class UNLServer {
    private transient String folderId;
    private transient SharedPreferences prefs;
    private transient List<GDFile> gdFiles;
    private transient static Drive drive;
    private static final String TAG = "unTag_UNLServer";
    private transient boolean allOK = false;


    public UNLServer(UNLApp app) {
        prefs = app.getApplicationContext().getSharedPreferences(UNLConsts.APP_PREF, Context.MODE_PRIVATE);
        drive = UNLApp.getDriveService();
        folderId = prefs.getString("folderId", "");
        gdFiles = new ArrayList<>();
    }

    public String getMD5FromServer() {

        String resultMD5 = "";
        String defaultString = "";

//        //save URLS from google disk to the SharedPreferences
//        saveURLS();
        try {
            printFilesInFolder(folderId);
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (allOK) {  //if getting GDfiles in saveURLS() is success
            for (int i = 0; i < gdFiles.size(); i++) {
                if (Arrays.asList(UNLConsts.UNL_ACCEPTED_FILE_EXTS)
                        .contains(gdFiles.get(i).getGdFileInst().getFileExtension())) {
                    resultMD5 = resultMD5 + "," + gdFiles.get(i).getGdFileMD5();
                }
            }
            Log.d(TAG, "md5 from server size " + String.valueOf(gdFiles.size()));
        } else {
            //if getting GDfiles in saveURLS() is fail return old MD5
            resultMD5 = prefs.getString("md5sApp", defaultString);
        }

        //add to the resultMD5 md5 from dd-files
        DirectoryWorks directoryWorks = new DirectoryWorks(
                UNLConsts.DIR_NAME);
        String[] files = directoryWorks.getDirFileList(true);   //(true) - get only audio-files
        for (int i = 0; i < files.length; i++) {
            String fileName = files[i].substring(files[i].lastIndexOf('/') + 1, files[i].length());
            if (fileName.startsWith(UNLConsts.PREFIX_USER_VIDEOFILES)) {
                FileWorks fileWorks = new FileWorks(files[i]);
                String tmpMD5 = fileWorks.getFileMD5();
                if (!resultMD5.contains(tmpMD5)) {
                    resultMD5 = resultMD5 + "," + tmpMD5;
                }
            }
        }
        //del last ","
        if (!resultMD5.isEmpty() && resultMD5.startsWith(",")) {
            resultMD5 = resultMD5.substring(1);
        }
        //overwrite md5sApp in pref if it different
        if (!prefs.getString("md5sApp", defaultString).equals(resultMD5)) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("md5sApp", resultMD5);
            editor.apply();
        }

        return resultMD5;
    }

    private void printFilesInFolder(String folderId)
            throws IOException, NullPointerException {
        Log.d(TAG, "print google drive folder");
        Children.List request = drive.children().list(folderId);

        do {
            try {
                ChildList children = request.execute();
                for (ChildReference child : children.getItems()) {
                    File file = drive.files().get(child.getId()).execute();
                    if (!file.getExplicitlyTrashed() && Arrays.asList(UNLConsts.UNL_ACCEPTED_FILE_EXTS).contains(file.getFileExtension())) {
                        gdFiles.add(new GDFile(file.getId(),
                                file.getTitle(),
                                file.getDownloadUrl(),
                                String.valueOf(file.getFileSize()),
                                file.getFileExtension(),
                                file.getMd5Checksum(), file));
                    }
                }
                request.setPageToken(children.getNextPageToken());
            } catch (IOException e) {
                System.out.println("An error occurred: " + e);
                request.setPageToken(null);
                allOK = false;
            }
        } while (request.getPageToken() != null && request.getPageToken().length() > 0);

        if (gdFiles.size() > 0) {
            allOK = true;
            //sort gdFiles by name
            Collections.sort(gdFiles, new Comparator<GDFile>() {
                @Override
                public int compare(GDFile lhs, GDFile rhs) {
                    return lhs.getGdFileName().compareTo(rhs.getGdFileName());
                }
            });
        } else {
            allOK = false;
        }
    }

    public List<GDFile> getGdFiles() {
        return gdFiles;
    }

}