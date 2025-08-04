package org.example.user.Service;

import org.example.user.Dto.SoftSkillDTO;
import org.example.user.Entity.SoftSkill;
import org.example.user.Repo.SoftSkillRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SoftSkillService {

    private final SoftSkillRepository softSkillRepository;

    public SoftSkillService(SoftSkillRepository softSkillRepository) {
        this.softSkillRepository = softSkillRepository;
    }

    public SoftSkillDTO createSoftSkill(SoftSkillDTO dto) {
        Optional<SoftSkill> existing = softSkillRepository.findByName(dto.getName());
        if (existing.isPresent()) {
            return new SoftSkillDTO(existing.get().getId(), existing.get().getName());
        }

        SoftSkill skill = new SoftSkill();
        skill.setName(dto.getName());

        SoftSkill saved = softSkillRepository.save(skill);
        return new SoftSkillDTO(saved.getId(), saved.getName());
    }

    public List<SoftSkillDTO> getAllSoftSkills() {
        return softSkillRepository.findAll().stream()
                .map(skill -> new SoftSkillDTO(skill.getId(), skill.getName()))
                .collect(Collectors.toList());
    }

    public Optional<SoftSkill> findByName(String name) {
        return softSkillRepository.findByName(name);
    }
}

