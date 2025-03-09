package zad1;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

public class Futil {

    public static void processDir(String dirName, String resultFileName) {
        System.out.println(dirName+ " "+ resultFileName);

        Path sciezkaKatalog = Paths.get(dirName);
        Path sciezkaPlikWynikowy = Paths.get(resultFileName);
        ArrayList<String> trescPliku = new ArrayList<>();

        // ------------------ usuniecie starego pliku wynikowego -----------------
        try {
            Files.deleteIfExists(sciezkaPlikWynikowy);
            System.out.println("usunieto stary plik wynikowy");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //------------------ chodzenie rekurencyjne po pliku --------------------

        try {
            Files.walkFileTree(sciezkaKatalog, new SimpleFileVisitor<Path>(){
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs){

                    ByteBuffer bb;
                    try (FileChannel fileChannel = FileChannel.open(file, StandardOpenOption.READ)) {
                        bb = ByteBuffer.allocate((int) fileChannel.size());
                        fileChannel.read(bb);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    bb.flip();
                    String zawartosc = Charset.forName("Cp1250").decode(bb).toString();
                    trescPliku.add(zawartosc);

                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    if (exc==null){
                        System.out.println("poprawnie sprawdzono sciezke: " + dir);
                        return FileVisitResult.CONTINUE;
                    }
                    else {
                        System.out.println("postVisitDirectoryError dla path: " + dir);
                        throw exc;
                    }
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String temp = "";

        for (String i: trescPliku ) {
            temp += i+"\n";
        }
        System.out.println("tresc pliku: \n" + temp);

        // ----------------------- zapis do pliku wynikowego --------------------------
        try {
            try (FileChannel fileChannel = FileChannel.open(sciezkaPlikWynikowy, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.READ, StandardOpenOption.TRUNCATE_EXISTING)) {
                ByteBuffer buffer = StandardCharsets.UTF_8.encode(temp);
                fileChannel.write(buffer);
            }

            System.out.println("poprawnie uzupelniono plik danymi \n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
