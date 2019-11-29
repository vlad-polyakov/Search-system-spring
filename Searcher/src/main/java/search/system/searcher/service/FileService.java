package search.system.searcher.service;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.UUID;
import java.util.stream.Stream;

@Component
public class FileService {
    private Path fileStorageLocation;
    private String uploadedDir = System.getProperty("user.dir") + "\\here";

    public String store(MultipartFile[] files) {
        try {
            fileStorageLocation = Paths.get(uploadedDir).toAbsolutePath().normalize();
            if(!Files.exists(fileStorageLocation)) {
                Files.createDirectory(fileStorageLocation);
            }
            else {
               // FileUtils.cleanDirectory(new File(fileStorageLocation.toString()));
            }
            System.out.println(files.length);
            for(MultipartFile file: files) {
                String fileName  = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("/")+1);
                Path path = fileStorageLocation.resolve(fileName);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }
            return fileStorageLocation.toString();
        } catch (IOException e) {
            System.out.println(e);
        }
        return fileStorageLocation.toString();
    }

}