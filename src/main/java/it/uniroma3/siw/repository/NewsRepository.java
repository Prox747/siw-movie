package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.News;
import org.springframework.data.repository.CrudRepository;

public interface NewsRepository extends CrudRepository<News, Long> {
    public boolean existsByTitleAndText(String title, String text);
}
