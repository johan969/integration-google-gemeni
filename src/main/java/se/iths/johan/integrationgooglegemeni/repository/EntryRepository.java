package se.iths.johan.integrationgooglegemeni.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.iths.johan.integrationgooglegemeni.model.Entry;

public interface EntryRepository extends JpaRepository<Entry, Long> {

}
