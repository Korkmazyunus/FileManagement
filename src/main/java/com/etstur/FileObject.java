package com.etstur;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "FileObject")
public class FileObject {
	@Id 
	@GeneratedValue(generator="system-uuid",strategy = GenerationType.AUTO)
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	@Column(name = "Id")
	private String id;

	@Column(name = "Name")
	private String name;
	
	@Column(name = "Path")
	private String path;
	
	@Column(name = "Extention")
	private String extention;
	
	@Column(name = "Length")
	private Long length;

	public FileObject() {
	}

	public FileObject(String name, String path,String extention,Long length) {
		this.name = name;
		this.path = path;
		this.extention = extention;
		this.length = length;
		
	}

	public String getId() {
		return id;
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
	
	@Override
	public String toString() {
		return "File [id=" + id 
				+ ", name=" + name 
				+ ", extention=" + extention
				+ ", length=" + length
				+ ", path=" + path 
				+ "]";
	}

}