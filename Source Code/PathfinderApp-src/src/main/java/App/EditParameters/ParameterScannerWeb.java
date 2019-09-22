package App.EditParameters;

import App.EditParameters.Secret.Secret;
import org.eclipse.egit.github.core.IRepositoryIdProvider;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryBranch;
import org.eclipse.egit.github.core.RepositoryContents;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.ContentsService;
import org.eclipse.egit.github.core.service.RepositoryService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

// Scans source code of ftc-app project to find parameters (using github api)
// TODO try again when github fixes their shit (ref parameter appears to be ignored)
public class ParameterScannerWeb {

    final static String repositoryName = "ftc_clueless_rr";
    final static String owner = "buggy213";
    final static String branch = "dev";

    String sha;

    public static void scan() {

        Secret secret = new Secret();
        secret.loadSecret();

        GitHubClient client = new GitHubClient();
        client.setCredentials(secret.getUsername(), secret.getPassword());

        RepositoryService repositoryService = new RepositoryService(client);
        Repository repo;
        try {
            repo = repositoryService.getRepository(owner, repositoryName);
        }
        catch (IOException e) {
            System.err.println("Unable to get repository - check internet connection?");
            return;
        }



        List<RepositoryContents> sourceFiles = recursiveSearchByFiletype(repo, client, "TeamCode");
        System.out.println("Done");
    }


    private static void downloadRepo(GitHubClient client) {

    }

    // Searches a repositories contents recursively by filetype using DFS
    private static List<RepositoryContents> recursiveSearchByFiletype(Repository repo, GitHubClient client, String path) {
        ContentsService contentsService = new ContentsService(client);

        List<RepositoryContents> sourceFiles = new ArrayList<>();
        List<RepositoryContents> contents = new ArrayList<>();
        LinkedList<RepositoryContents> directoriesQueue = new LinkedList<>();

        try {
            contents = contentsService.getContents(() -> String.valueOf(repo.generateId()), "", branch);
            if (!path.isEmpty()) {
                RepositoryContents directory = null;
                for (RepositoryContents content : contents) {
                    if (content.getPath().equals(path)) {
                        directory = content;
                        break;
                    }

                    if (content.getType().equals("dir")) {
                        directoriesQueue.push(content);
                    }
                }
                if (directory == null) {
                    System.err.println("Unable to find directory specified by path");
                    return null;
                }
                contents = new ArrayList<>();
                contents.add(directory);
                directoriesQueue = new LinkedList<>();

            }

        }
        catch (IOException e) {
            System.err.println("Unable to get contents of repository");
            return contents;
        }
        do {
            for (RepositoryContents content : contents) {
                // Check if it is a source code file
                if (content.getName().endsWith(".java")) {
                    sourceFiles.add(content);
                }

                if (content.getType().equals("dir")) {
                    directoriesQueue.push(content);
                }
            }

            // Check next subdirectory in queue
            RepositoryContents subdirectory = directoriesQueue.pop();
            try {
                contents = contentsService.getContents(() -> String.valueOf(repo.generateId()), subdirectory.getPath(), branch);
            }
            catch (IOException e) {
                System.err.println("Unable to get contents of subdirectory: " + subdirectory.getName()
                        + " " + subdirectory.getPath());

                return contents;
            }
        }
        while (!directoriesQueue.isEmpty());
        return sourceFiles;
    }
}
