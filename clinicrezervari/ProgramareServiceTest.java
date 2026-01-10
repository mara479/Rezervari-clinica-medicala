package ro.clinicrezervari;

import org.junit.jupiter.api.Test;
import ro.clinicrezervari.db.DatabaseManager;
import ro.clinicrezervari.db.ProgramareDao;
import ro.clinicrezervari.model.Programare;
import ro.clinicrezervari.service.ProgramareService;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProgramareServiceTest {
    @Test
    void nuPermiteSuprapuneri() throws Exception {
        DatabaseManager.initSchema();

        ProgramareService service = new ProgramareService(new ProgramareDao());

        Programare p1 = new Programare(0, 1, 1,
                LocalDate.now(), LocalTime.of(10, 0), "ACTIVA");

        Programare p2 = new Programare(0, 2, 1,
                LocalDate.now(), LocalTime.of(10, 0), "ACTIVA");

        service.creeazaProgramare(p1);

        assertThrows(IllegalStateException.class, () -> {
            try {
                service.creeazaProgramare(p2);
            } catch (Exception e) {
                // dacă apare SQLException aici, vrem să pice testul, nu să o înghită
                throw new RuntimeException(e);
            }
        });
    }
}