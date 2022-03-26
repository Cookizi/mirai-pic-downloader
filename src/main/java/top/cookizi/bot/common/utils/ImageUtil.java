package top.cookizi.bot.common.utils;

import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.common.bytesource.ByteSourceFile;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.jpeg.exif.ExifRewriter;
import org.apache.commons.imaging.formats.png.PngConstants;
import org.apache.commons.imaging.formats.png.PngImageInfo;
import org.apache.commons.imaging.formats.png.PngImageParser;
import org.apache.commons.imaging.formats.png.PngText;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.MicrosoftTagConstants;
import org.apache.commons.imaging.formats.tiff.constants.TiffDirectoryConstants;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputDirectory;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputSet;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteOrder;
import java.util.*;

public class ImageUtil {

    public static String write(byte[] imgBytes, String url, String name, String savePath, String comment) throws IOException, ImageReadException, ImageWriteException {
        var imageFormat = (ImageFormats) Imaging.guessFormat(imgBytes);
        String filename;
        if (imageFormat == ImageFormats.UNKNOWN) {
            filename = getFilename(url);
        } else {
            filename = name + "." + imageFormat.getExtension().toLowerCase(Locale.ROOT);
        }
        if (!savePath.endsWith("/")) {
            savePath = savePath + "/";
        }
        var dst = new File(savePath + filename);
        if (StringUtils.isBlank(comment)) {
            writeDirectly(imgBytes, dst);
            return filename;
        }
        switch (imageFormat) {
            case JPEG:
                writeJpeWithComment(imgBytes, dst, comment);
                break;
            case PNG:
                writePngWithComment(imgBytes, dst, comment);
                break;
            case GIF:
//                writeGifWithComment();
            default:
                writeDirectly(imgBytes, dst);
        }
        return filename;
    }

    private static String getFilename(String url) {
        //"https://video.twimg.com/ext_tw_video/id/pu/vid/1272x720/filename.mp4?tag=12"
        String[] split = url.split("\\?")[0].split("/");
        return split[split.length - 1];
    }

    public static String readComment(File file) throws IOException, ImageReadException {
        var imageFormat = (ImageFormats) Imaging.guessFormat(file);
        switch (imageFormat) {
            case JPEG:
                return readJpgComment(file);
            case PNG:
                return readPngComment(file);
            default:
                return null;
        }
    }

    /**
     * 写入来源到图片exif内
     *
     * @param jpegImageFile A source image file.
     * @param dst           The output file.
     * @throws IOException
     * @throws ImageReadException
     * @throws ImageWriteException
     */
    public static void writeJpeWithComment(final byte[] jpegImageFile, final File dst, final String comment) throws IOException,
            ImageReadException, ImageWriteException {
        try (FileOutputStream fos = new FileOutputStream(dst);
             OutputStream os = new BufferedOutputStream(fos)) {
            TiffOutputSet outputSet = null;

            final ImageMetadata metadata = Imaging.getMetadata(jpegImageFile);
            final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
            if (null != jpegMetadata) {
                final TiffImageMetadata exif = jpegMetadata.getExif();

                if (null != exif) {
                    outputSet = exif.getOutputSet();
                }
            }

            if (null == outputSet) {
                outputSet = new TiffOutputSet();
            }

            TiffOutputDirectory rootDirectory = outputSet.getRootDirectory();
            if (rootDirectory == null) {
                outputSet.addRootDirectory();
            }

            var directory = outputSet.findDirectory(TiffDirectoryConstants.DIRECTORY_TYPE_DIR_0);
            if (directory == null) {
                directory = new TiffOutputDirectory(TiffDirectoryConstants.DIRECTORY_TYPE_DIR_0, ByteOrder.BIG_ENDIAN);
            }

            directory.removeField(MicrosoftTagConstants.EXIF_TAG_XPCOMMENT);
            directory.add(MicrosoftTagConstants.EXIF_TAG_XPCOMMENT, comment);

            new ExifRewriter().updateExifMetadataLossless(jpegImageFile, os, outputSet);
        }
    }

    public static String readJpgComment(final File file) throws IOException, ImageReadException {
        final JpegImageMetadata metadata = (JpegImageMetadata) Imaging.getMetadata(file);
        var exifValue = metadata.findEXIFValue(MicrosoftTagConstants.EXIF_TAG_XPCOMMENT);
        if (exifValue == null) {
            return null;
        } else {
            return exifValue.getStringValue();
        }
    }

    public static void writePngWithComment(final byte[] imgBytes, final File out, String url) throws IOException, ImageReadException, ImageWriteException {
        var outputStream = new FileOutputStream(out);

        var parser = new PngImageParser();
        BufferedImage bufferedImage = parser.getBufferedImage(imgBytes, null);

        List<PngText> list = new ArrayList<>();
        list.add(new PngText.Itxt("URL", url, "", ""));
        Map<String, Object> pa = new HashMap<>();
        pa.put(PngConstants.PARAM_KEY_PNG_TEXT_CHUNKS, list);
        parser.writeImage(bufferedImage, outputStream, pa);
    }

    public static String readPngComment(final File file) throws IOException, ImageReadException {
        var parser = new PngImageParser();
        var imageInfo = (PngImageInfo) parser.getImageInfo(new ByteSourceFile(file));
        if (imageInfo == null) return null;
        return imageInfo.getTextChunks().stream()
                .filter(x -> "URL".equals(x.keyword))
                .map(x -> x.text)
                .findFirst().orElse(null);
    }

    public static void writeDirectly(byte[] imgBytes, File dst) throws IOException {
        var fileOutputStream = new FileOutputStream(dst);
        var stream = new BufferedOutputStream(fileOutputStream);
        stream.write(imgBytes);
        stream.flush();
        stream.close();
        fileOutputStream.flush();
        fileOutputStream.close();
    }


}
