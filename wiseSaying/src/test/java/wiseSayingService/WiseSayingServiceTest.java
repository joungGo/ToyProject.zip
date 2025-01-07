package wiseSayingService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import wiseSayingRepository.WiseSayingRepository;
import data.Proverb;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class WiseSayingServiceTest {
    private WiseSayingService service;
    private WiseSayingRepository repository;
    private List<Proverb> proverbList;

    @BeforeEach
    public void setUp() {
        repository = Mockito.mock(WiseSayingRepository.class);
        proverbList = new ArrayList<>();
        service = new WiseSayingService(repository, proverbList);
    }

    @Test
    public void testRegisterProverb() {
        // Given
        String proverb = "Actions speak louder than words.";
        String author = "Unknown";
        int lastId = 1;

        when(repository.readLastId()).thenReturn(lastId);

        // When
        service.registerProverb(proverb, author);

        // Then
        assertEquals(1, proverbList.size());
        Proverb registeredProverb = proverbList.get(0);
        assertEquals(lastId, registeredProverb.getId());
        assertEquals(proverb, registeredProverb.getProverb());
        assertEquals(author, registeredProverb.getAuthor());

        verify(repository, times(1)).saveProverb(registeredProverb);
        verify(repository, times(1)).saveLastId(lastId + 1);
    }
}