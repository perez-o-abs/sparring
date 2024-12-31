package us.abstracta.sparring.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import us.abstracta.sparring.model.Article;

@RunWith(SpringRunner.class)
@WebMvcTest(Controller.class)
public class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Controller controller;

    private Article testArticle;

    @Before
    public void setup() {
        testArticle = new Article();
        testArticle.setId(1);
        testArticle.setTitle("Test Phone");
        testArticle.setCategory("Phones");
        testArticle.setPrice(599);
        testArticle.setDescription("Test Description");
        testArticle.setImage("test.jpg");
    }

    @Test
    public void testGetArticles() throws Exception {
        List<Article> articles = Arrays.asList(testArticle);
        when(controller.getArticles()).thenReturn(articles);

        mockMvc.perform(MockMvcRequestBuilders.get("/articles/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Phone"));
    }

    @Test
    public void testGetArticlesByCat() throws Exception {
        List<Article> articles = Arrays.asList(testArticle);
        when(controller.getArticlesByCat("Phones")).thenReturn(articles);

        mockMvc.perform(MockMvcRequestBuilders.get("/articles/all/category")
                .param("cat", "Phones")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].category").value("Phones"));
    }

    @Test
    public void testGetArticleById() throws Exception {
        when(controller.getArticleById(1)).thenReturn(testArticle);

        mockMvc.perform(MockMvcRequestBuilders.get("/articles/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Phone"));
    }
}
