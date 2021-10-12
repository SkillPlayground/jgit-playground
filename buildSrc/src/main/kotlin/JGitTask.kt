import java.io.File
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.Constants
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.revwalk.RevCommit
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

open class JGitTask : DefaultTask() {

    @TaskAction
    fun run() {
        File("${project.rootProject.rootDir}/README.md").apply { writeText("# HELLO") }

        val gitFolder =
            File("${project.rootProject.rootDir}").walkTopDown().first { it.name == ".git" }

        val repository: Repository =
            FileRepositoryBuilder().setGitDir(gitFolder).readEnvironment().findGitDir().build()

        val head = repository.resolve(Constants.HEAD).name

        logger.lifecycle(repository.resolve(Constants.HEAD).name)

        val commits: List<RevCommit> =
            Git(repository).log().add(repository.resolve(head)).call().toList()

        logger.lifecycle(commits.first().fullMessage)

        commits.forEach { logger.lifecycle(it.fullMessage) }
    }
}
