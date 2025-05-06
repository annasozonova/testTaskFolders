import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FolderTask1 {
    private static final List<Path> ROOT_DIRS = List.of(
            Paths.get("folders","A"),
            Paths.get("folders","B")
    );
    public static void main(String[] args) throws IOException {
        List<Path> jsFolders = new ArrayList<>();

        for (Path root : ROOT_DIRS) findJsFolders(root, jsFolders);

        System.out.println("Folders with .js files: ");
        for(Path path : jsFolders) System.out.println(path.toString());
    }

    private static void findJsFolders(Path root, List<Path> result) throws IOException {
        if (!Files.isDirectory(root)) return;

        try (DirectoryStream<Path> entries = Files.newDirectoryStream(root)){
            boolean hasSubFolders = false;
            boolean hasJsFile = false;

            for (Path entry : entries){
                if (Files.isDirectory(entry)) {
                    hasSubFolders = true;
                    findJsFolders(entry, result);
                } else if (entry.toString().endsWith(".js")) {
                    hasJsFile = true;
                }
            }

            if (!hasSubFolders && hasJsFile) {
                result.add(root);
            }
        }
    }
}