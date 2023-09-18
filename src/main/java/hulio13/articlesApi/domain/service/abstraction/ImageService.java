package hulio13.articlesApi.domain.service.abstraction;

import java.io.IOException;
import java.io.InputStream;

public interface ImageService {
    String upload(String relativePath, String nameWithExtension, InputStream stream) throws IOException;

    boolean delete(String relativePath, String nameWithExtension);
}
