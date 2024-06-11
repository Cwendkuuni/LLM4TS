import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarOutputStream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Test case for TarArchiveOutputStream class.
 */
public class TarArchiveOutputStreamTest {

    private OutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    /**
     * Test putArchiveEntry method with valid parameters.
     */
    @Test
    public void testPutArchiveEntryValid() throws IOException {
        // Put an entry with valid parameters
        File testFile = new File("testFile.txt");
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("testEntry.txt");
        tarArchiveEntry.setFile(testFile);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);

        // Write some data to the entry
        byte[] data = "This is a test entry".getBytes();
        tarArchiveOutputStream.write(data, 0, data.length);

        // Close the entry
        tarArchiveOutputStream.closeArchiveEntry();

        // Read the data back from the output stream
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveEntry readEntry = new TarArchiveEntry(inputStream);
        byte[] readData = new byte[data.length];
        inputStream.read(readData);

        // Compare the data with the original data
        Assert.assertArrayEquals(data, readData);
    }

    /**
     * Test putArchiveEntry method with long file name.
     */
    @Test
    public void testPutArchiveEntryLongFileName() throws IOException {
        // Set long file mode to LONGFILE_GNU
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        // Put an entry with a long file name
        String longFileName = "a/b/c/d/e/f/g/h/i/j/k/l/m/n/o/p/q/r/s/t/u/v/w/x/y/z/aa/ab/ac/ad/ae/af/ag/ah/ai/aj/ak/al/am/an/ao/ap/aq/ar/as/at/au/av/aw/ax/ay/az/ba/bb/bc/bd/be/bf/bg/bh/bi/bj/bk/bl/bm/bn/bo/bp/bq/br/bs/bt/bu/bv/bw/bx/by/bz/ca/cb/cc/cd/ce/cf/cg/ch/ci/cj/ck/cl/cm/cn/co/cp/cq/cr/cs/ct/cu/cv/cw/cx/cy/cz/da/db/dc/dd/de/df/dg/dh/di/dj/dk/dl/dm/dn/do/dp/dq/dr/ds/dt/du/dv/dw/dx/dy/dz/ea/eb/ec/ed/ee/ef/eg/eh/ei/ej/ek/el/em/en/eo/ep/eq/er/es/et/eu/ev/ew/ex/ey/ez/fa/fb/fc/fd/fe/ff/fg/fh/fi/fj/fk/fl/fm/fn/fo/fp/fq/fr/fs/ft/fu/fv/fw/fx/fy/fz/ga/gb/gc/gd/ge/gf/gg/gh/gi/gj/gk/gl/gm/gn/go/gp/gq/gr/gs/gt/gu/gv/gw/gx/gy/gz/ha/hb/hc/hd/he/hf/hg/hh/hi/hj/hk/hl/hm/hn/ho/hp/hq/hr/hs/ht/hu/hv/hw/hx/hy/hz/ia/ib/ic/id/ie/if/ig/ih/ii/ij/ik/il/im/in/io/ip/iq/ir/is/it/iu/iv/iw/ix/iy/iz/ja/jb/jc/jd/je/jf/jg/jh/ji/jj/jk/jl/jm/jn/jo/jp/jq/jr/js/jt/ju/jv/jw/jx/jy/jz/ka/kb/kc/kd/ke/kf/kg/kh/ki/kj/kk/kl/km/kn/ko/kp/kq/kr/ks/kt/ku/kv/kw/kx/ky/kz/la/lb/lc/ld/le/lf/lg/lh/li/lj/lk/ll/lm/ln/lo/lp/lq/lr/ls/lt/lu/lv/lw/lx/ly/lz/ma/mb/mc/md/me/mf/mg/mh/mi/mj/mk/ml/mm/mn/mo/mp/mq/mr/ms/mt/mu/mv/mw/mx/my/mz/na/nb/nc/nd/ne/nf/ng/nh/ni/nj/nk/nl/nm/nn/no/np/nq/nr/ns/nt/nu/nv/nw/nx/ny/nz/oa/ob/oc/od/oe/of/og/oh/oi/oj/ok/ol/om/on/oo/op/oq/or/os/ot/ou/ov/ow/ox/oy/oz/pa/pb/pc/pd/pe/pf/pg/ph/pi/pj/pk/pl/pm/pn/po/pp/pq/pr/ps/pt/pu/pv/pw/px/py/pz/qa/qb/qc/qd/qe/qf/qg/qh/qi/qj/qk/ql/qm/qn/qo/qp/qq/qr/qs/qt/qu/qv/qw/qx/qy/qz/ra/rb/rc/rd/re/rf/rg/rh/ri/rj/rk/rl/rm/rn/ro/rp/rq/rr/rs/rt/ru/rv/rw/rx/ry/rz/sa/sb/sc/sd/se/sf/sg/sh/si/sj/sk/sl/sm/sn/so/sp/sq/sr/ss/st/su/sv/sw/sx/sy/sz/ta/tb/tc/td/te/tf/tg/th/ti/tj/tk/tl/tm/tn/to/tp/tq/tr/ts/tt/tu/tv/tw/tx/ty/tz/ua/ub/uc/ud/ue/uf/ug/uh/ui/uj/uk/ul/um/un/uo/up/uq/ur/us/ut/uu/uv/uw/ux/uy/uz/va/vb/vc/vd/ve/vf/vg/vh/vi/vj/vk/vl/vm/vn/vo/vp/vq/vr/vs/vt/vu/vv/vw/vx/vy/vz/wa/wb/wc/wd/we/wf/wg/wh/wi/wj/wk/wl/wm/wn/wo/wp/wq/wr/ws/wt/wu/wv/ww/wx/wy/wz/xa/xb/xc/xd/xe/xf/xg/xh/xi/xj/xk/xl/xm/xn/xo/xp/xq/xr/xs/xt/xu/xv/xw/xw/xz/ya/yb/yc/yd/ye/yf/yg/yh/yi/yj/yk/yl/ym/yn/yo/yp/yq/yr/ys/yt/yu/yv/yw/yx/yy/yz/za/zb/zc/zd/ze/zf/zg/zh/zi/zj/zk/zl/zm/zn/zo/zp/zq/zr/zs/zt/zu/zv/zw/zx/zy/zz";
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(longFileName);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);

        // Write some data to the entry
        tarArchiveOutputStream.write(new byte[1024]);

        // Close the entry
        tarArchiveOutputStream.closeArchiveEntry();

        // Read the data back from the output stream
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveEntry readEntry = new TarArchiveEntry(inputStream);

        // Verify the file name is correct
        Assert.assertEquals(longFileName, readEntry.getName());

        // Read the data
        byte[] readData = new byte[1024];
        int numRead = inputStream.read(readData, 0, readData.length);

        // Compare the data with the original data
        Assert.assertArrayEquals(readData, new byte[1024]);
    }

    /**
     * Test putArchiveEntry method with invalid file name length.
     */
    @Test(expected = RuntimeException.class)
    public void testPutArchiveEntryInvalidFileNameLength() throws IOException {
        // Put an entry with an invalid file name length
        String invalidFileName = "a/b/c/d/e/f/g/h/i/j/k/l/m/n/o/p/q/r/s/t/u/v/w/x/y/z/ab";
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(invalidFileName);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
    }

    /**
     * Test finish method.
     */
    @Test
    public void testFinish() throws IOException {
        // Put some entries and write some data
        File testFile1 = new File("testFile1.txt");
        TarArchiveEntry tarArchiveEntry1 = new TarArchiveEntry("testEntry1.txt");
        tarArchiveEntry1.setSize(1024);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry1);
        tarArchiveOutputStream.write(new byte[1024]);
        tarArchiveOutputStream.closeArchiveEntry();

        File testFile2 = new File("testFile2.txt");
        TarArchiveEntry tarArchiveEntry2 = new TarArchiveEntry("testEntry2.txt");
        tarArchiveEntry2.setSize(2048);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry2);
        tarArchiveOutputStream.write(new byte[2048]);
        tarArchiveOutputStream.closeArchiveEntry();

        // Finish the archive
        tarArchiveOutputStream.finish();

        // Read the archive back from the output stream
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        // Verify the archive has the expected number of entries
        int numEntries = 0;
        while (true) {
            TarArchiveEntry readEntry = new TarArchiveEntry(inputStream);
            if (readEntry == null) {
                break;
            }
            numEntries++;
        }

        Assert.assertEquals(2, numEntries);
    }

    /**
     * Test close method with unclosed entry.
     */
    @Test(expected = IOException.class)
    public void testCloseWithUnclosedEntry() throws IOException {
        // Put an entry and write some data
        File testFile = new File("testFile.txt");
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("testEntry.txt");
        tarArchiveEntry.setFile(testFile);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.write(new byte[1024]);

        // Close the output stream instead of the entry
        tarArchiveOutputStream.close();
    }

    /**
     * Test close method after finish.
     */
    @Test
    public void testCloseAfterFinish() throws IOException {
        // Put some entries and write some data
        File testFile1 = new File("testFile1.txt");
        TarArchiveEntry tarArchiveEntry1 = new TarArchiveEntry("testEntry1.txt");
        tarArchiveEntry1.setSize(1024);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry1);
        tarArchiveOutputStream.write(new byte[1024]);
        tarArchiveOutputStream.closeArchiveEntry();

        File testFile2 = new File("testFile2.txt");
        TarArchiveEntry tarArchiveEntry2 = new TarArchiveEntry("testEntry2.txt");
        tarArchiveEntry2.setSize(2048);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry2);
        tarArchiveOutputStream.write(new byte[2048]);
        tarArchiveOutputStream.closeArchiveEntry();

        // Finish the archive
        tarArchiveOutputStream.finish();

        // Close the output stream
        tarArchiveOutputStream.close();
    }
}