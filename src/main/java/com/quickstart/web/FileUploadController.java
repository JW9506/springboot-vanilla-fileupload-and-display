package com.quickstart.web;

import java.io.IOException;
import java.util.stream.Collectors;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.quickstart.storage.StorageHandleException;
import com.quickstart.storage.StorageService;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class FileUploadController {

  private final StorageService storageService;

  @GetMapping("/")
  public String listUploadedFiles(Model model) throws IOException {

    model.addAttribute("files", storageService.loadAll()
        .map(path -> MvcUriComponentsBuilder
            .fromMethodName(FileUploadController.class, "serveFile", path.getFileName().toString())
            .build().toString())
        .collect(Collectors.toList()));

    return "uploadForm";
  }


  @GetMapping("/files/{filename:.+}")
  @ResponseBody
  public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

    Resource file = storageService.loadAsResource(filename);
    return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
        "attachment; filename=\"" + file.getFilename() + "\"").body(file);
  }

  @PostMapping("/")
  public String handleFileUpload(@RequestParam("file") MultipartFile file,
      RedirectAttributes redirectAttributes) {

    try {
      storageService.store(file);
      redirectAttributes.addFlashAttribute("message",
          "You successfully uploaded " + file.getOriginalFilename() + "!");
    } catch (StorageHandleException e) {
      redirectAttributes.addFlashAttribute("message", e.getMessage());
    }

    return "redirect:/";
  }

}
