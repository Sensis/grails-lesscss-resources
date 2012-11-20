import org.grails.plugin.resource.ResourceMeta
import org.grails.plugin.resource.mapper.MapperPhase
import org.junit.Test
import org.gmock.WithGMock
import com.asual.lesscss.LessEngine

/**
 * @author Paul Fairless
 */
@WithGMock
class LesscssResourceMapperTests extends GroovyTestCase {

    LesscssResourceMapper mapper
    def lessCompiler

    void setUp() {
        lessCompiler = mock(LessEngine)
        mapper = new LesscssResourceMapper(lessCompiler)
        mapper.metaClass.log = [debug: {}, error: {}]
    }

    @Test
    void testMapperGeneratesCssFromLessResource() {
        String fileName = "file.less"
        def targetFile = mock(File, constructor('/var/file/file_less.css'))
        targetFile.exists().returns(true).stub()

        File originalFile = mock(File)
        originalFile.getAbsolutePath().returns('/var/file/' + fileName).stub()

        File processedFile = mock(File)
        processedFile.getName().returns(fileName).stub()
        processedFile.getAbsolutePath().returns('/var/file/' + fileName).stub()
        lessCompiler.compile(originalFile, targetFile, false).once()

        def config = [:]
        def resource = new ResourceMeta(contentType: '', tagAttributes: [rel: 'stylesheet/less'])
        def resourceMock = mock(resource)
        resourceMock.processedFile.returns(processedFile)
        resourceMock.setProcessedFile(targetFile)
        resourceMock.sourceUrl.returns(fileName)
        resourceMock.updateActualUrlFromProcessedFile().once()

        mock(mapper).getOriginalFileSystemFile(fileName).returns(originalFile)

        play {
            mapper.map(resourceMock, config)
            assertEquals 'stylesheet', resource.tagAttributes.rel
            assertEquals 'text/css', resource.contentType
        }
    }

    @Test
    void testMapperRunsEarlyInProcessingPipeline() {
        assertEquals MapperPhase.GENERATION, mapper.phase
    }

    @Test
    void testMapperIncludesLessCSS() {
        assertEquals(['**/*.less'], mapper.defaultIncludes)
    }

    @Test
    void testGeneratedFilename() {
        assertEquals 'foo/bar_less.css', mapper.generateCompiledFileFromOriginal('foo/bar.less')
        assertEquals 'foo/bar_less.css', mapper.generateCompiledFileFromOriginal('foo/bar.LESS')
        assertEquals 'foo/./bar_less.css', mapper.generateCompiledFileFromOriginal('foo/./bar.less')
        assertEquals 'foo/less/bar_less.css', mapper.generateCompiledFileFromOriginal('foo/less/bar.less')
    }

}
