package com.etstur;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
 
@RestController
@RequestMapping("/api/File")
@Api(value = "File Controller APIs", produces = MediaType.APPLICATION_JSON_VALUE, tags = {"File"}) 
public class FileObjectController {
	
	@Autowired
	FileObjectRepository fileRepository;
	
	@Autowired
    private FileStorageService fileStorageService;
	
	@ApiOperation(value = "Get all files")
    @RequestMapping(method = RequestMethod.GET, value = "/getall")
	public ResponseEntity<List<FileObject>> GetAllTutorials() {
		try {
			List<FileObject> files = fileRepository.findAll();

			if (files.isEmpty()) {
				return new ResponseEntity<List<FileObject>>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<List<FileObject>>(files, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<List<FileObject>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@ApiOperation(value = "upload file (add and update)")
    @RequestMapping(method = RequestMethod.POST, value = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE} )
	public ResponseEntity<String> Upload(@RequestParam("upload file") MultipartFile file) {
		try {
			String fileName = fileStorageService.storeFile(file);
			
			Resource resource = fileStorageService.loadFileAsResource(fileName);
			
			String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
			String orginalFileName = fileName.substring(0,fileName.lastIndexOf("."));
			
			fileRepository.save(new FileObject(
						orginalFileName,
						resource.getFile().getPath(),
						extension,
						resource.getFile().length()
						));
			
			return new ResponseEntity<String>("File was succesfully writed to system", HttpStatus.CREATED);
		} catch (IOException ex) {
			return new ResponseEntity<String>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		 catch (Exception e) {
			 return new ResponseEntity<String>("An error occured",HttpStatus.INTERNAL_SERVER_ERROR);
		 }
	}
	
	@ApiOperation(value = "Delete file")
    @RequestMapping(method = RequestMethod.DELETE, value = "/delete")
	public ResponseEntity<HttpStatus> Delete(@RequestParam("file id")  String id) {
		try {
			Optional<FileObject> fileObject =	fileRepository.findById(id);
			
			if(fileObject == null || fileObject.isEmpty())
				return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
			
			fileRepository.delete(fileObject.get());
			
			File myObj = new File(fileObject.get().getPath()); 
			
		    if (myObj.delete()) 
		      return new ResponseEntity<HttpStatus>(HttpStatus.OK);
		   
		    return new ResponseEntity<HttpStatus>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			return new ResponseEntity<HttpStatus>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@ApiOperation(value = "Read file")
    @RequestMapping(method = RequestMethod.GET, value = "/getbyid")
	public ResponseEntity<FileObjectDto> getFileById(@RequestParam("file id")  String id) {
		try {
			Optional<FileObject> fileObject = fileRepository.findById(id);
		
		
			if(fileObject == null || fileObject.isEmpty())
				return new ResponseEntity<FileObjectDto>(HttpStatus.NOT_FOUND);
			
			Resource resource = fileStorageService.loadFileAsResource(fileObject.get().getName() +"."+fileObject.get().getExtension());
			InputStream fileAsStream = resource.getInputStream();
			byte[] content = fileAsStream.readAllBytes();
			
			FileObjectDto dto = new FileObjectDto(fileObject.get().getId(),
					fileObject.get().getName(),
					fileObject.get().getPath(),
					fileObject.get().getExtension(),
					fileObject.get().getLength(),
					content);
			
			return new ResponseEntity<FileObjectDto>(dto, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<FileObjectDto>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}