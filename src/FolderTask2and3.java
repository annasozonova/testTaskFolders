import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class FolderTask2and3 {
    private static final List<Path> ROOT_DIRS = List.of(
            Paths.get("folders","A"),
            Paths.get("folders","B")
    );

    private static final int N = 3;

    public static void main(String[] args) throws IOException {
        Map<Path, Integer> jsFolders = new LinkedHashMap<>();

        for (Path root : ROOT_DIRS) findJsFolders(root, jsFolders);

        System.out.println("Folders with .js files and their count: ");
        for (Map.Entry<Path, Integer> entry : jsFolders.entrySet()) {
            System.out.println(entry.getKey() + ", .js files count: " + entry.getValue());
        }

        System.out.println("\nSplit into " + N + " groups (by total file count): ");
        List<List<Map.Entry<Path, Integer>>> groups = splitIntoGroupsByFilesCount(jsFolders, N);
        printGroups(groups);

        System.out.println("\nSplit into " + N + " groups (by path count): ");
        groups = splitByPathsCount(jsFolders, N);
        printGroups(groups);
    }

    private static void findJsFolders(Path root, Map<Path, Integer> result) throws IOException {
        if (!Files.isDirectory(root)) return;

        try (DirectoryStream<Path> entries = Files.newDirectoryStream(root)){
            boolean hasSubFolders = false;
            int filesCount = 0;

            List<Path> allEntries = new ArrayList<>();
            for (Path entry : entries){
                allEntries.add(entry);
            }

            for (Path entry : allEntries){
                if (Files.isDirectory(entry)){
                    hasSubFolders = true;
                    findJsFolders(entry, result);
                } else if (entry.toString().endsWith(".js"))
                    filesCount++;
            }

            if (!hasSubFolders && filesCount > 0)
                result.put(root, filesCount);
        }
    }

    private static List<List<Map.Entry<Path, Integer>>> splitIntoGroupsByFilesCount(Map<Path, Integer> folders, int n){
        List<List<Map.Entry<Path,Integer>>> groups = new ArrayList<>();
        List<Integer> groupSums = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            groups.add(new ArrayList<>());
            groupSums.add(0);
        }

        for(Map.Entry<Path, Integer> entry : folders.entrySet()) {
            int minIndex = 0;
            for (int i = 1; i < n; i++) {
                if (groupSums.get(i) < groupSums.get(minIndex))
                    minIndex = i;
            }

            groups.get(minIndex).add(entry);
            groupSums.set(minIndex, groupSums.get(minIndex) + entry.getValue());
        }
        return groups;
    }

    private static List<List<Map.Entry<Path, Integer>>> splitByPathsCount(Map<Path, Integer> folderMap, int n) {
        List<List<Map.Entry<Path, Integer>>> groups = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            groups.add(new ArrayList<>());
        }

        int index = 0;
        for (Map.Entry<Path, Integer> entry : folderMap.entrySet()) {
            groups.get(index % n).add(entry);
            index++;
        }

        return groups;
    }

    private static void printGroups(List<List<Map.Entry<Path, Integer>>> groups) {
        for (int i = 0; i < groups.size(); i++) {
            System.out.println("Group " + (i + 1) + ":");
            for (Map.Entry<Path, Integer> entry : groups.get(i)) {
                System.out.println("  " + entry.getKey() + " -> " + entry.getValue());
            }
        }
    }
}