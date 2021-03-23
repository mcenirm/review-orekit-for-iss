package mcenirm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.orekit.bodies.BodyShape;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.data.DataContext;
import org.orekit.data.DirectoryCrawler;
import org.orekit.files.ccsds.ODMMetaData;
import org.orekit.files.ccsds.OEMFile;
import org.orekit.files.ccsds.OEMFile.EphemeridesBlock;
import org.orekit.files.ccsds.OEMFile.OemSatelliteEphemeris;
import org.orekit.files.ccsds.OEMParser;
import org.orekit.frames.Frame;
import org.orekit.frames.FramesFactory;
import org.orekit.models.earth.ReferenceEllipsoid;
import org.orekit.propagation.BoundedPropagator;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScale;
import org.orekit.time.TimeScalesFactory;
import org.orekit.utils.CartesianDerivativesFilter;
import org.orekit.utils.IERSConventions;
import org.orekit.utils.TimeStampedPVCoordinates;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) throws FileNotFoundException {
        // configure the reference context
        final File home = new File(System.getProperty("user.home"));
        final File orekitData = new File(home, "orekit-data");
        if (!orekitData.exists()) {
            System.err.format(Locale.US, "Failed to find %s folder%n", orekitData.getAbsolutePath());
            System.err.format(Locale.US,
                    "You need to download %s from %s, unzip it in %s and rename it 'orekit-data' for this tutorial to work%n",
                    "orekit-data-master.zip",
                    "https://gitlab.orekit.org/orekit/orekit-data/-/archive/master/orekit-data-master.zip",
                    home.getAbsolutePath());
            System.exit(1);
        }
        DataContext.getDefault().getDataProvidersManager().addProvider(new DirectoryCrawler(orekitData));

        Frame inertialFrame = FramesFactory.getEME2000();
        TimeScale utc = TimeScalesFactory.getUTC();

        Frame ecf = FramesFactory.getITRF(IERSConventions.IERS_2010, true);
        BodyShape earth = ReferenceEllipsoid.getWgs84(ecf);

        String fileName = (new File(home, "derwrics/2021-03-20.ISS.OEM_J2K_EPH.txt")).toString();
        OEMCleaner cleaner = new OEMCleaner(fileName);
        OEMParser parser = new OEMParser().withConventions(IERSConventions.IERS_2010);
        OEMFile oem = parser.parse(cleaner, fileName);
        Map<String, OemSatelliteEphemeris> satellites = oem.getSatellites();
        OemSatelliteEphemeris issEphemeris = satellites.get("1998-067-A");
        String id = issEphemeris.getId();
        double mu = issEphemeris.getMu();
        BoundedPropagator propagator = issEphemeris.getPropagator();
        List<EphemeridesBlock> segments = issEphemeris.getSegments();
        AbsoluteDate start = issEphemeris.getStart();
        AbsoluteDate stop = issEphemeris.getStop();
        System.out.println("id:          " + id);
        System.out.println("mu:          " + mu);
        System.out.println("propagator:  " + propagator);
        System.out.println("start:       " + start);
        System.out.println("stop:        " + stop);
        System.out.println("stop-start:  " + stop.offsetFrom(start, utc));
        System.out.println("segments:");
        for (EphemeridesBlock block : segments) {
            CartesianDerivativesFilter blAvailableDerivatives = block.getAvailableDerivatives();
            Class blClass = block.getClass();
            List<TimeStampedPVCoordinates> blCoordinates = block.getCoordinates();
            List blCovarianceMatrices = block.getCovarianceMatrices();
            List blEphemeridesDataLines = block.getEphemeridesDataLines();
            List blEphemeridesDataLinesComment = block.getEphemeridesDataLinesComment();
            Frame blFrame = block.getFrame();
            String blFrameCenterString = block.getFrameCenterString();
            String blFrameString = block.getFrameString();
            boolean blHasRefFrameEpoch = block.getHasRefFrameEpoch();
            Frame blInertialFrame = block.getInertialFrame();
            int blInterpolationDegree = block.getInterpolationDegree();
            String blInterpolationMethod = block.getInterpolationMethod();
            int blInterpolationSamples = block.getInterpolationSamples();
            ODMMetaData blMetaData = block.getMetaData();
            double blMu = block.getMu();
            AbsoluteDate blStart = block.getStart();
            AbsoluteDate blStartTime = block.getStartTime();
            AbsoluteDate blStop = block.getStop();
            AbsoluteDate blStopTime = block.getStopTime();
            TimeScale blTimeScale = block.getTimeScale();
            String blTimeScaleString = block.getTimeScaleString();
            AbsoluteDate blUseableStartTime = block.getUseableStartTime();
            AbsoluteDate blUseableStopTime = block.getUseableStopTime();
            System.out.println("  availableDerivatives:         " + blAvailableDerivatives);
            System.out.println("  class:                        " + blClass);
            System.out.println("  coordinates:                  " + listSize(blCoordinates));
            System.out.println("  covarianceMatrices:           " + listSize(blCovarianceMatrices));
            System.out.println("  ephemeridesDataLines:         " + listSize(blEphemeridesDataLines));
            System.out.println("  ephemeridesDataLinesComment:  " + listSize(blEphemeridesDataLinesComment));
            System.out.println("  frame:                        " + blFrame);
            System.out.println("  frameCenterString:            " + blFrameCenterString);
            System.out.println("  frameString:                  " + blFrameString);
            System.out.println("  hasRefFrameEpoch:             " + blHasRefFrameEpoch);
            System.out.println("  inertialFrame:                " + blInertialFrame);
            System.out.println("  interpolationDegree:          " + blInterpolationDegree);
            System.out.println("  interpolationMethod:          " + blInterpolationMethod);
            System.out.println("  interpolationSamples:         " + blInterpolationSamples);
            System.out.println("  metaData:                     " + blMetaData);
            System.out.println("  mu:                           " + blMu);
            System.out.println("  start:                        " + blStart);
            System.out.println("  startTime:                    " + blStartTime);
            System.out.println("  stop:                         " + blStop);
            System.out.println("  stopTime:                     " + blStopTime);
            System.out.println("  timeScale:                    " + blTimeScale);
            System.out.println("  timeScaleString:              " + blTimeScaleString);
            System.out.println("  useableStartTime:             " + blUseableStartTime);
            System.out.println("  useableStopTime:              " + blUseableStopTime);

            System.out.println();

            int i = 1258;
            TimeStampedPVCoordinates timeStampedPVCoordinates = blCoordinates.get(i);
            System.out.println("  coordinates[" + i + "]:               " + timeStampedPVCoordinates);

            GeodeticPoint satLatLonAlt = earth.transform(timeStampedPVCoordinates.getPosition(),
                    FramesFactory.getEME2000(), timeStampedPVCoordinates.getDate());
            System.out.println("                                " + satLatLonAlt);
            System.out.println("                                " + (long) (timeStampedPVCoordinates.getDate()
                    .durationFrom(TimeScalesFactory.getTimeScales().getJavaEpoch())));
        }
    }

    private static String listSize(List x) {
        return x == null ? "null" : ("[ " + x.size() + " ]");
    }
}
