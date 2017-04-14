/**
 * 
 */
package ippoz.madness.lite.support;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Tommy
 *
 */
public class ZipUtils {

    private List <String> fileList;

    private ZipUtils() {
        fileList = new ArrayList < String > ();
    }

    public static File zipFile(File dirToZip, String zipFilename) {
        ZipUtils appZip = new ZipUtils();
        appZip.generateFileList(dirToZip, dirToZip);
        appZip.zipIt(dirToZip, zipFilename);
        return new File(zipFilename);
    }

    private void zipIt(File dirToZip, String zipFile) {
        byte[] buffer = new byte[1024];
        String source = dirToZip.getName();
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        try {
            fos = new FileOutputStream(zipFile);
            zos = new ZipOutputStream(fos);

            FileInputStream in = null;

            for (String file: this.fileList) {
                ZipEntry ze = new ZipEntry(source + File.separator + file);
                zos.putNextEntry(ze);
                try {
                    in = new FileInputStream(dirToZip.getPath() + File.separator + file);
                    int len;
                    while ((len = in .read(buffer)) > 0) {
                        zos.write(buffer, 0, len);
                    }
                } finally {
                    in.close();
                }
            }

            zos.closeEntry();

            AppLogger.logInfo(getClass(), "Zipping Completed");
            
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                zos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void generateFileList(File dirToZip, File node) {
        // add file only
        if (node.isFile()) {
            fileList.add(generateZipEntry(dirToZip, node.toString()));
        }

        if (node.isDirectory()) {
            String[] subNote = node.list();
            for (String filename: subNote) {
                generateFileList(dirToZip, new File(node, filename));
            }
        }
    }

    private String generateZipEntry(File dirToZip, String file) {
        return file.substring(dirToZip.getPath().length() + 1, file.length());
    }
}