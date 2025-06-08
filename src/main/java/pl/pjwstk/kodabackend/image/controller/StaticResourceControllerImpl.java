package pl.pjwstk.kodabackend.image.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.pjwstk.kodabackend.image.service.StaticResourceService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/images")
class StaticResourceControllerImpl implements StaticResourceController {

    private final StaticResourceService staticResourceService;

    @GetMapping("/view/{filename}")
    @Override
    public ResponseEntity<Resource> viewImage(@PathVariable String filename) {
        return staticResourceService.serveImage(filename);
    }

    @GetMapping("/list")
    @Override
    public ResponseEntity<List<String>> listImages() {
        return staticResourceService.listImages();
    }

    @GetMapping("/exists/{filename}")
    @Override
    public ResponseEntity<Boolean> checkImageExists(@PathVariable String filename) {
        return staticResourceService.checkImageExists(filename);
    }
}