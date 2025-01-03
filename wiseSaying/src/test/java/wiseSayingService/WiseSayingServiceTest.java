package wiseSayingService;

import data.Proverb;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import wiseSayingRepository.WiseSayingRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WiseSayingServiceTest {

    private WiseSayingService service;
    private WiseSayingRepository mockRepository;

    @BeforeEach
    void setUp() {
        mockRepository = mock(WiseSayingRepository.class);
        service = new WiseSayingService(mockRepository, new ArrayList<>());
    }

    @Test
    void registerProverb_shouldAddProverbToListAndSave() {
        // given
        String proverbText = "행동은 말보다 더 크게 말한다.";
        String author = "속담";
        int mockId = 1;

        when(mockRepository.readLastId()).thenReturn(mockId);

        // when
        service.registerProverb(proverbText, author);

        // then
        verify(mockRepository, times(1)).saveLastId(mockId);
        verify(mockRepository, times(1)).saveProverb(any(Proverb.class));

        List<Proverb> proverbs = service.getProverbList();
        assertEquals(1, proverbs.size());
        assertEquals(proverbText, proverbs.get(0).getProverb());
        assertEquals(author, proverbs.get(0).getAuthor());
    }
}
