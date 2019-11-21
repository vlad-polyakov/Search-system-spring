package search.system.searcher.service;

import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Component
public class FileService {
    private Path fileStorageLocation;
    public String store(MultipartFile file) {
        try {
            fileStorageLocation = Paths.get(file.getOriginalFilename()).toAbsolutePath().normalize();
            return fileStorageLocation.toString();
        } catch (Exception e) {
            throw new RuntimeException("FAIL!");
        }
    }

}