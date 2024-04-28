package zw.co.tayasoft.services;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import zw.co.tayasoft.data.Ward;
import zw.co.tayasoft.data.WardRepository;

@Service
public class WardService {

    private final WardRepository repository;

    public WardService(WardRepository repository) {
        this.repository = repository;
    }

    public Optional<Ward> get(Long id) {
        return repository.findById(id);
    }

    public Ward update(Ward entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<Ward> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Ward> list(Pageable pageable, Specification<Ward> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
