package zw.co.tayasoft.data;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface VillageRepository extends JpaRepository<Village, Long>, JpaSpecificationExecutor<Village> {

}
