package org.example.user.Controller;

import org.example.user.Dto.SoftSkillDTO;
import org.example.user.Service.SoftSkillService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/soft-skills")
public class SoftSkillController {

    private final SoftSkillService softSkillService;

    public SoftSkillController(SoftSkillService softSkillService) {
        this.softSkillService = softSkillService;
    }

    @PostMapping
    public SoftSkillDTO createSoftSkill(@RequestBody SoftSkillDTO dto) {
        return softSkillService.createSoftSkill(dto);
    }

    @GetMapping
    public List<SoftSkillDTO> getAllSoftSkills() {
        return softSkillService.getAllSoftSkills();
    }
}

