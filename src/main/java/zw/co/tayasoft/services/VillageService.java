package zw.co.tayasoft.services;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import zw.co.tayasoft.data.Village;
import zw.co.tayasoft.data.VillageRepository;

@Service
public class VillageService {

    private final VillageRepository repository;

    public VillageService(VillageRepository repository) {
        this.repository = repository;
    }

    public Optional<Village> get(Long id) {
        return repository.findById(id);
    }

    public Village update(Village entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<Village> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Village> list(Pageable pageable, Specification<Village> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
