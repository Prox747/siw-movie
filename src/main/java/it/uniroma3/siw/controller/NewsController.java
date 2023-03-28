package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.News;
import it.uniroma3.siw.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class NewsController {
    @Autowired NewsRepository newsRepository;
    @GetMapping("/news")
    public String showNews(Model model) {
        model.addAttribute("allNews", newsRepository.findAll());
        return "news.html";
    }

    @GetMapping("/news/{id}")
    public String getSingleNews(@PathVariable("id") Long id, Model model) {
        model.addAttribute("news", this.newsRepository.findById(id).get());
        return "singleNews.html";
    }

    @GetMapping("/formAddNews")
    public String formAddNews(Model model){
        model.addAttribute("news", new News());
        return "formAddNews.html";
    }

    @PostMapping("/addedNews")
    public String addNews(@ModelAttribute("news") News news, Model model) {
        if (!newsRepository.existsByTitleAndText(news.getTitle(), news.getText())) {
            this.newsRepository.save(news);
            model.addAttribute("movie", news);
            return "singleNews.html";
        } else {
            model.addAttribute("messaggioErrore", "Questa news esiste già");
            return "formAddNews.html";
        }
    }
}
