package testbug;

import com.graphhopper.GraphHopper;
import com.graphhopper.reader.dem.ElevationProvider;
import com.graphhopper.reader.osm.GraphHopperOSM;
import com.graphhopper.util.CmdArgs;
import com.graphhopper.util.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Test {
    private static final Logger logger = LoggerFactory.getLogger(Test.class);

    public static void main(String[] args) throws IOException {
        CmdArgs argsFromProps = CmdArgs.read(Helper.readFile("config.properties").toArray(new String[]{}));
        argsFromProps.merge(CmdArgs.read(args));
        logger.info("read " + argsFromProps);
        GraphHopper graphHopper = new GraphHopperOSM() {
            @Override
            protected void cleanUp() {
                List<Raster> list = new ArrayList<>();

                try {
                    // read it twice to be sure that we trigger the bug
                    for (int i = 0; i < 2; i++) {
                        for (String rasterFile : Arrays.asList("i0.tif", "i1.tif", "i2.tif", "i3.tif", "i4.tif", "i5.tif", "i6.tif", "i7.tif", "i8.tif")) {
                            logger.info(new Date() + " " + i + " reading ... " + rasterFile + " " + Helper.getMemInfo());
                            BufferedImage image = ImageIO.read(new BufferedInputStream(new FileInputStream(rasterFile), 5000));
                            list.add(image.getRaster());
                        }
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                logger.info(new Date() + " read " + list.size() + " finished");
                super.cleanUp();
            }
        }.init(argsFromProps);
        if (graphHopper.getElevationProvider() == ElevationProvider.NOOP)
            throw new RuntimeException("Enable the elevation provider as otherwise the bug is not triggered");

        logger.info("start " + new Date().toString());
        graphHopper.importOrLoad();
        logger.info("end" + new Date().toString());
        graphHopper.close();
    }
}
