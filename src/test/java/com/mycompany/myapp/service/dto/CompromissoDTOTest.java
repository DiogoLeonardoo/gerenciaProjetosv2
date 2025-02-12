package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CompromissoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompromissoDTO.class);
        CompromissoDTO compromissoDTO1 = new CompromissoDTO();
        compromissoDTO1.setId(1L);
        CompromissoDTO compromissoDTO2 = new CompromissoDTO();
        assertThat(compromissoDTO1).isNotEqualTo(compromissoDTO2);
        compromissoDTO2.setId(compromissoDTO1.getId());
        assertThat(compromissoDTO1).isEqualTo(compromissoDTO2);
        compromissoDTO2.setId(2L);
        assertThat(compromissoDTO1).isNotEqualTo(compromissoDTO2);
        compromissoDTO1.setId(null);
        assertThat(compromissoDTO1).isNotEqualTo(compromissoDTO2);
    }
}
