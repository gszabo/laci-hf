import com.prezi.homeassignment.schemalib.*;

import com.google.common.collect.Streams;
import com.google.common.io.Resources;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ApiTest {

    @Test
    public void ConstructorTest() throws IOException {

        compare("ConstructorTest1.json",
            new Directory("/", new FileSystemObjectVector())
        );

        compare("ConstructorTest2.json",
            new Directory("/", new FileSystemObjectVector(
                new Directory("subdir", new FileSystemObjectVector(
                    new File("file.txt")
                ))
            )));

        compare("ConstructorTest3.json",
            new Directory("/", new FileSystemObjectVector(
                new Directory("subdir", new FileSystemObjectVector(
                    new File("file.txt")
                ))
            )));

        compare("ConstructorTest4.json",
            new Directory("/", new FileSystemObjectVector()));

    }

    @Test
    public void ArrayOperations() throws IOException {
        Directory d1 = new Directory("/", new FileSystemObjectVector());
        Directory d2 = new Directory("subdir", new FileSystemObjectVector());

        d1.getContent().insert(0, new File("file1.txt"));
        compare("ArrayOperations1.json", d1);

        d1.getContent().insert(1, d2);
        compare("ArrayOperations2.json", d1);

        d2.getContent().insert(0, new File("file2.txt"));
        compare("ArrayOperations3.json", d1);

        d1.getContent().remove(0);
        compare("ArrayOperations4.json", d1);
    }

    @Test
    public void TraverseWithFor() {
        Directory d = new Directory("/", new FileSystemObjectVector(
            new Directory("subdir1", new FileSystemObjectVector(
                new File("file1.txt"),
                new Directory("subdir2", new FileSystemObjectVector(
                    new File("file2.txt")
                )),
                new File("file3.txt")
            )),
            new Directory("subdir3", new FileSystemObjectVector(
                new File("file4.txt")
            ))
        ));

        int itemCount = getItemCountWithForLoop(d);
        Assert.assertEquals("item count should be", 8, itemCount);
    }

    @Test
    public void TraverseWithIterator() {
        Directory d = new Directory("/", new FileSystemObjectVector(
            new Directory("subdir1", new FileSystemObjectVector(
                new File("file1.txt"),
                new Directory("subdir2", new FileSystemObjectVector(
                    new File("file2.txt")
                )),
                new File("file3.txt")
            )),
            new Directory("subdir3", new FileSystemObjectVector(
                new File("file4.txt")
            ))
        ));


        int itemCount2 = getItemCountWithIterator(d);
        Assert.assertEquals("item count should be", 8, itemCount2);
    }

    @Test
    public void FieldUpdate() throws IOException {
        Directory d = new Directory("/", new FileSystemObjectVector(
            new Directory("subdir1", new FileSystemObjectVector(
                new File("file1.txt"),
                new Directory("subdir2", new FileSystemObjectVector(
                    new File("file2.txt")
                )),
                new File("file3.txt")
            )),
            new Directory("subdir3", new FileSystemObjectVector(
                new File("file4.txt")
            ))
        ));

        renameAll("hello", d);
        compare("FieldUpdate.json", d);
    }


    // TODO: ignore if you don't implement "Task: Runtime checks"
    @Test(expected=UnsupportedOperationException.class)
    public void CannotSetPropertyToNull() {
        Directory d = new Directory("/", new FileSystemObjectVector());

        d.setName(null);
    }

    // TODO: ignore if you don't implement "Task: Runtime checks"
    @Test(expected=UnsupportedOperationException.class)
    public void CannotSetListPropertyToNull() {
        Directory d = new Directory("/", new FileSystemObjectVector());

        d.setContent(null);
    }

    // TODO: ignore if you don't implement "Task: Runtime checks"
    @Test(expected=UnsupportedOperationException.class)
    public void CannotInsertIfAttached() {
        Directory d1 = new Directory("/", new FileSystemObjectVector());
        Directory d2 = new Directory("subdir", new FileSystemObjectVector());
        d1.getContent().insert(0, d2);
        d1.getContent().insert(0, d2);
    }

    // TODO: ignore if you don't implement "Task: Runtime checks"
    @Test
    public void CanReinsertAfterRemove() {
        Directory d1 = new Directory("/", new FileSystemObjectVector());
        Directory d2 = new Directory("subdir", new FileSystemObjectVector());
        d1.getContent().insert(0, d2);
        d1.getContent().remove(0);
        d1.getContent().insert(0, d2);
    }

    // TODO: ignore if you don't implement "Task: Runtime checks"
    @Test(expected=UnsupportedOperationException.class)
    public void CannotInsertUnderItself() {
        Directory d1 = new Directory("/", new FileSystemObjectVector());
        Directory d2 = new Directory("subdir", new FileSystemObjectVector());
        d1.getContent().insert(0, d2);
        d2.getContent().insert(0, d1);
    }

    private void renameAll(String newName, FileSystemObject fileSystemObject) {
        fileSystemObject.setName(newName);
        if (fileSystemObject instanceof Directory) {
            Directory d = (Directory) fileSystemObject;
            Streams.stream(d.getContent()).forEach(child -> renameAll(newName, child));
        }
    }

    private int getItemCountWithForLoop(FileSystemObject fileSystemObject) {
        if (fileSystemObject instanceof File) {
            return 1;
        }

        Directory d = (Directory) fileSystemObject;
        int itemCount = 1;
        for (int i = 0; i < d.getContent().size(); i++) {
            itemCount += getItemCountWithForLoop(d.getContent().get(i));
        }
        return itemCount;
    }

    private int getItemCountWithIterator(FileSystemObject fileSystemObject) {
        if (fileSystemObject instanceof File) {
            return 1;
        }

        Directory d = (Directory) fileSystemObject;

        return Streams.stream(d.getContent())
            .map(this::getItemCountWithIterator)
            .reduce(1, Integer::sum);
    }

    private void compare(String resourceName, Directory d) throws IOException {
        String jsonDataExpected = Resources.toString(Resources.getResource(resourceName), StandardCharsets.UTF_8);

        String jsonDataActual = Directory.fromJson(d.toJson()).toJson();

        Assert.assertEquals(resourceName, jsonDataExpected, jsonDataActual);
    }
}
