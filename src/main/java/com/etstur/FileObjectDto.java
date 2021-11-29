package com.etstur;

public class FileObjectDto {
	
	private String id;
	private String name;
	private String path;
	private String extention;
	private Long length;
	private byte[] content;

	public FileObjectDto() {
	}

	public FileObjectDto(String id,String name, String path,String extention,Long length,byte[] content) {
		this.id = id;
		this.name = name;
		this.path = path;
		this.extention = extention;
		this.length = length;
		this.content = content;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
	
	public void setContent(byte[] content) {
		this.content = content;
	}

	public byte[] getContent() {
		return content;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public String getExtension() {
		return extention;
	}

	public void setExtension(String extention) {
		this.extention = extention;
	}

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}
}