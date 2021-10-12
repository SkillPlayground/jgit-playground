import java.io.File
import org.eclipse.jgit.lib.Ref
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.revwalk.RevCommit
import org.eclipse.jgit.revwalk.RevWalk
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

open class JGitTask : DefaultTask() {

    @TaskAction
    fun run() {
        File("${project.rootProject.rootDir}/README.md").apply {
            writeText("# HELLO")
        }

        val gitFolder =
            File("${project.rootProject.rootDir}").walkTopDown().first { it.name == ".git" }

        val repository: Repository =
            FileRepositoryBuilder().setGitDir(gitFolder).readEnvironment().findGitDir().build()

        val head: Ref = repository.findRef(repository.fullBranch)
        val latestCommit: RevCommit = RevWalk(repository).parseCommit(head.objectId)

        logger.lifecycle(latestCommit.fullMessage)
    }
}
