package com.example.demo.utils;

import com.example.demo.models.PacketType;
import com.example.demo.models.Statement;
import com.example.demo.models.User;

public interface PdfGenerator {

    void generatePdfDocs(Statement statement, String key);
}
