package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EtapaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EtapaDTO.class);
        EtapaDTO etapaDTO1 = new EtapaDTO();
        etapaDTO1.setId(1L);
        EtapaDTO etapaDTO2 = new EtapaDTO();
        assertThat(etapaDTO1).isNotEqualTo(etapaDTO2);
        etapaDTO2.setId(etapaDTO1.getId());
        assertThat(etapaDTO1).isEqualTo(etapaDTO2);
        etapaDTO2.setId(2L);
        assertThat(etapaDTO1).isNotEqualTo(etapaDTO2);
        etapaDTO1.setId(null);
        assertThat(etapaDTO1).isNotEqualTo(etapaDTO2);
    }
}
