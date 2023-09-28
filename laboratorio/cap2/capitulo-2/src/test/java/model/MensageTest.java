package model;

import com.hibernate.Capitulo2Application;
import com.hibernate.capitulo2.model.Menssage;
import com.hibernate.capitulo2.services.MenssageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@AutoConfigureWebTestClient
@SpringBootTest(classes = Capitulo2Application.class)
class MensageTest {

    @Autowired
    private MenssageService menssageService;
    @Test
    void testGetId() {
        Menssage menssage = new Menssage();
        menssage.setTexto("Teste"+ uuid());
        menssageService.save(menssage);
        List<Menssage> allMenssage = menssageService.findAllMenssage();
        assert(!allMenssage.isEmpty());
    }

    String uuid() {
        return UUID.randomUUID().toString();
    }
}
