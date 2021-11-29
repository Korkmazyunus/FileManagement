package com.etstur;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) throws Exception {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
            .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new Exception("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String storeFile(MultipartFile file) throws Exception {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        
        if (fileName.contains("..")) {
            throw new IOException("Sorry! Filename contains invalid path sequence " + fileName);
        }
        
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        
        if(!extension.toLowerCase().equals("png")  &&
        		!extension.toLowerCase().equals("jpeg")  &&
        		!extension.toLowerCase().equals("jpg")  &&
        		!extension.toLowerCase().equals("docx")  &&
        		!extension.toLowerCase().equals("pdf")  &&
        		!extension.toLowerCase().equals("xlsx")  
        	) {
        	throw new IOException("Invalid file extenson:"+ extension +". Valid file extension type: png,jpeg,jpg.docx,pdf,xlsx" );
        }

        try {
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new IOException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Resource loadFileAsResource(String fileName) throws Exception {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists()) {
                return resource;
            } else {
                throw new Exception("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new Exception("File not found " + fileName, ex);
        }
    }
}
