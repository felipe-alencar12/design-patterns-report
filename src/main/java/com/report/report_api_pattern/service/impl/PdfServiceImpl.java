package com.report.report_api_pattern.service.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.report.report_api_pattern.client.DeepSeekClient;
import com.report.report_api_pattern.dto.ExperienceDTO;
import com.report.report_api_pattern.dto.ResumeRequestDTO;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class PdfServiceImpl {

    private final DeepSeekClient deepSeekClient;

    public PdfServiceImpl(DeepSeekClient deepSeekClient) {
        this.deepSeekClient = deepSeekClient;
    }

    public byte[] generateResumePdf(ResumeRequestDTO resumeRequest) {
        String aboutMe = generateAboutMe(resumeRequest);

        List<ExperienceDTO> completedExperiences = enrichExperiences(resumeRequest.getExperiences());

        // Agora gerar o texto completo com esses dados
        StringBuilder builder = new StringBuilder();

        builder.append("Nome: ").append(resumeRequest.getName()).append("\n");
        builder.append("Email: ").append(resumeRequest.getEmail()).append("\n");
        builder.append("Telefone: ").append(resumeRequest.getPhone()).append("\n\n");

        builder.append("Sobre mim:\n").append(aboutMe).append("\n\n");

        builder.append("Experiências:\n");
        for (ExperienceDTO exp : completedExperiences) {
            builder.append("Empresa: ").append(exp.getCompany()).append("\n");
            builder.append("Cargo: ").append(exp.getRole()).append("\n");
            builder.append("Período: ").append(exp.getStartDate()).append(" até ");
            builder.append(exp.isCurrentJob() ? "atualmente" : exp.getEndDate()).append("\n");
            builder.append("Atuação: ").append(exp.getDescription()).append("\n\n");
        }

        builder.append("Habilidades:\n");
        for (String skill : resumeRequest.getSkills()) {
            builder.append("- ").append(skill).append("\n");
        }

        return createPdfFromText(builder.toString());
    }

    private String generateAboutMe(ResumeRequestDTO dto) {
        String prompt = String.format("""
            Gere um texto para a seção 'Sobre mim' de um currículo, não escreva 'Sobre mim' na sua resposta.
            Nome: %s
            Habilidades: %s
            Cargo atual: %s

            Escreva de forma profissional e objetiva.
            """,
                dto.getName(),
                String.join(", ", dto.getSkills()),
                getCurrentJobRole(dto.getExperiences())
        );

        return deepSeekClient.ask(prompt);
    }

    private List<ExperienceDTO> enrichExperiences(List<ExperienceDTO> experiences) {
        List<ExperienceDTO> enriched = new ArrayList<>();

        for (ExperienceDTO exp : experiences) {
            String prompt = String.format("""
                Gere uma descrição profissional de atividades para um currículo, baseado nas informações abaixo, lembre que você está gerando uma resposta
                que será utilizada no currículo, então não repita dados, seja conciso e gere apenas a descriçõa sem informar os dados de Cargo e empresa e não
                escreva a palavara 'Descrição' no começo e nem coloquei '-':
                Cargo: %s
                Empresa: %s
                """, exp.getRole(), exp.getCompany());

            String description = deepSeekClient.ask(prompt);
            exp.setDescription(description);
            enriched.add(exp);
        }

        return enriched;
    }

    private String getCurrentJobRole(List<ExperienceDTO> experiences) {
        return experiences.stream()
                .filter(ExperienceDTO::isCurrentJob)
                .findFirst()
                .map(ExperienceDTO::getRole)
                .orElse("Profissional da área");
    }

    private byte[] createPdfFromText(String text) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter.getInstance(document, outputStream);
            document.open();

            // Fontes
            Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.DARK_GRAY);
            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
            Font italicFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 11, BaseColor.GRAY);

            String[] lines = text.split("\n");

            for (String line : lines) {
                line = line.trim();

                if (line.startsWith("Nome:")) {
                    String value = line.substring(5).trim();
                    Paragraph p = new Paragraph();
                    p.add(new Chunk("Nome: ", boldFont));
                    p.add(new Chunk(value, normalFont));
                    p.setSpacingAfter(10);
                    document.add(p);
                }

                else if (line.equalsIgnoreCase("Sobre mim:") ||
                        line.equalsIgnoreCase("Experiências:") ||
                        line.equalsIgnoreCase("Habilidades:")) {
                    Paragraph p = new Paragraph(line, sectionFont);
                    p.setSpacingBefore(15);
                    p.setSpacingAfter(8);
                    document.add(p);
                }

                else if (line.startsWith("Email:")) {
                    Paragraph p = new Paragraph();
                    p.add(new Chunk("Email: ", boldFont));
                    p.add(new Chunk(line.substring(6).trim(), normalFont));
                    p.setSpacingAfter(4);
                    document.add(p);
                }

                else if (line.startsWith("Telefone:")) {
                    Paragraph p = new Paragraph();
                    p.add(new Chunk("Telefone: ", boldFont));
                    p.add(new Chunk(line.substring(9).trim(), normalFont));
                    p.setSpacingAfter(4);
                    document.add(p);
                }

                else if (line.startsWith("Empresa:")) {
                    Paragraph p = new Paragraph();
                    p.add(new Chunk("Empresa: ", boldFont));
                    p.add(new Chunk(line.substring(8).trim(), normalFont));
                    p.setIndentationLeft(15);
                    document.add(p);
                }

                else if (line.startsWith("Cargo:")) {
                    Paragraph p = new Paragraph();
                    p.add(new Chunk("Cargo: ", boldFont));
                    p.add(new Chunk(line.substring(6).trim(), normalFont));
                    p.setIndentationLeft(15);
                    document.add(p);
                }

                else if (line.startsWith("Período:")) {
                    Paragraph p = new Paragraph();
                    p.add(new Chunk("Período: ", boldFont));
                    p.add(new Chunk(line.substring(8).trim(), normalFont));
                    p.setIndentationLeft(15);
                    document.add(p);
                }

                else if (line.startsWith("Atuação:")) {
                    Paragraph p = new Paragraph();
                    p.add(new Chunk("Atuação: ", boldFont));
                    p.add(new Chunk(line.substring(8).trim(), normalFont));
                    p.setIndentationLeft(15);
                    p.setSpacingAfter(8);
                    document.add(p);
                }

                else if (line.startsWith("-")) {
                    // Habilidades
                    Paragraph p = new Paragraph(line.substring(2).trim(), italicFont);
                    p.setIndentationLeft(15);
                    document.add(p);
                }

                else if (!line.isEmpty()) {
                    // Texto livre (como 'Sobre mim')
                    Paragraph p = new Paragraph(line, normalFont);
                    p.setSpacingAfter(6);
                    document.add(p);
                }
            }

            document.close();
            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao gerar PDF", e);
        }
    }


}
