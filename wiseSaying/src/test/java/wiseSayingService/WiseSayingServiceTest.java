package wiseSayingService;

import data.Proverb;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import wiseSayingRepository.MockWiseSayingRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WiseSayingServiceTest {
    private WiseSayingService service;
    private MockWiseSayingRepository mockRepository;

    @BeforeEach
    public void setup() {
        mockRepository = new MockWiseSayingRepository();
        service = new WiseSayingService(mockRepository, mockRepository); // Mock 주입
    }

    @Test
    public void testRegisterProverb() {
        // Given
        String proverb = "The early bird catches the worm.";
        String author = "Unknown";

        // When
        service.registerProverb(proverb, author);

        // Then
        List<Proverb> proverbs = mockRepository.loadProverbs();
        assertEquals(1, proverbs.size());
        assertEquals(proverb, proverbs.get(0).getProverb());
        assertEquals(author, proverbs.get(0).getAuthor());
    }

    @Test
    public void testDeleteProverb() {
        // Given
        service.registerProverb("Proverb 1", "Author 1");
        service.registerProverb("Proverb 2", "Author 2");

        // When
        service.deleteProverb(1);

        // Then
        List<Proverb> proverbs = mockRepository.loadProverbs();
        assertEquals(1, proverbs.size());
        assertEquals("Proverb 2", proverbs.get(0).getProverb());
    }

    @Test
    public void testSearchProverbByAuthor() {
        // Given
        service.registerProverb("Proverb 1", "Author A");
        service.registerProverb("Proverb 2", "Author B");

        // When
        service.searchProverb("author", "Author A");

        // Then
        List<Proverb> proverbs = mockRepository.loadProverbs();
        assertEquals(2, proverbs.size());
    }

    @Test
    public void testListProverbs() {
        // Given
        service.registerProverb("Proverb 1", "Author A");
        service.registerProverb("Proverb 2", "Author B");
        service.registerProverb("Proverb 3", "Author C");

        // When
        service.listProverbs(1); // 1페이지 호출

        // Then
        List<Proverb> proverbs = mockRepository.loadProverbs();
        assertEquals(3, proverbs.size());
    }
}
