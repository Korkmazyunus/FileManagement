package com.etstur;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FileObjectRepository extends JpaRepository<FileObject, String> {
  FileObject findByName(String name);
}