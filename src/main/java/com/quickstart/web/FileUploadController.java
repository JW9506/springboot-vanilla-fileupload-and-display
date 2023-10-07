package com.quickstart.web;

import java.io.IOException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FileUploadController {
  @GetMapping("/")
  public String listUploadedFiles(Model model) throws IOException {

    return "uploadForm";
  }
}
