import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TarArchiveOutputStreamTest {

    @Test
    public void testPutArchiveEntryAndClose() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(10);
        entry.setName("test.txt");

        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write("Hello, World!".getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.close();

        byte[] expectedTarEntry = new byte[1024];
        System.arraycopy(TarConstants.MAGIC, 0, expectedTarEntry, 0, TarConstants.MAGIC.length);
        TarArchiveEntry expectedEntry = new TarArchiveEntry("test.txt");
        expectedEntry.setSize(10);
        expectedEntry.setName("test.txt");
        expectedEntry.writeEntryHeader(expectedTarEntry, 0);

        byte[] expectedData = "Hello, World!".getBytes(StandardCharsets.UTF_8);
        byte[] expectedDataBlock = new byte[TarConstants.BLOCKSIZE];
        System.arraycopy(expectedData, 0, expectedDataBlock, 0, expectedData.length);

        byte[] actualTarEntry = outputStream.toByteArray();
        Assert.assertArrayEquals(expectedTarEntry, actualTarEntry, "Invalid tar entry header.");

        ByteArrayInputStream inputStream = new ByteArrayInputStream(actualTarEntry);
        TarArchiveEntry actualEntry = new TarArchiveEntry((InputStream) inputStream);
        byte[] actualData = new byte[actualEntry.getSize()];
        inputStream.read(actualData);

        Assert.assertArrayEquals(expectedData, actualData, "Invalid data written to the archive.");
    }

    @Test(expected = RuntimeException.class)
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        String longFileName = "a/b/c/d/e/f/g/h/i/j/k/l/m/n/o/p/q/r/s/t/u/v/w/x/y/z/aa/ab/ac/ad/ae/af/ag/ah/ai/aj/ak/al/am/an/ao/ap/aq/ar/as/at/au/av/aw/ax/ay/az/ba/bb/bc/bd/be/bf/bg/bh/bi/bj/bk/bl/bm/bn/bo/bp/bq/br/bs/bt/bu/bv/bw/bx/by/bz/ca/cb/cc/cd/ce/cf/cg/ch/ci/cj/ck/cl/cm/cn/co/cp/cq/cr/cs/ct/cu/cv/cw/cx/cy/cz/da/db/dc/dd/de/df/dg/dh/di/dj/dk/dl/dm/dn/do/dp/dq/dr/ds/dt/du/dv/dw/dx/dy/dz/ea/eb/ec/ed/ee/ef/eg/eh/ei/ej/ek/el/em/en/eo/ep/eq/er/es/et/eu/ev/ew/ex/ey/ez/fa/fb/fc/fd/fe/ff/fg/fh/fi/fj/fk/fl/fm/fn/fo/fp/fq/fr/fs/ft/fu/fv/fw/fx/fy/fz/ga/gb/gc/gd/ge/gf/gg/gh/gi/gj/gk/gl/gm/gn/go/gp/gq/gr/gs/gt/gu/gv/gw/gx/gy/gz/ha/hb/hc/hd/he/hf/hg/hh/hi/hj/hk/hl/hm/hn/ho/hp/hq/hr/hs/ht/hu/hv/hw/hx/hy/hz/ia/ib/ic/id/ie/if/ig/ih/ii/ij/ik/il/im/in/io/ip/iq/ir/is/it/iu/iv/iw/ix/iy/iz/ja/jb/jc/jd/je/jf/jg/jh/ji/jj/jk/jl/jm/jn/jo/jp/jq/jr/js/jt/ju/jv/jw/jx/jy/jz/ka/kb/kc/kd/ke/kf/kg/kh/ki/kj/kk/kl/km/kn/ko/kp/kq/kr/ks/kt/ku/kv/kw/kx/ky/kz/la/lb/lc/ld/le/lf/lg/lh/li/lj/lk/ll/lm/ln/lo/lp/lq/lr/ls/lt/lu/lv/lw/lx/ly/lz/ma/mb/mc/md/me/mf/mg/mh/mi/mj/mk/ml/mm/mn/mo/mp/mq/mr/ms/mt/mu/mv/mw/mx/my/mz/na/nb/nc/nd/ne/nf/ng/nh/ni/nj/nk/nl/nm/nn/no/np/nq/nr/ns/nt/nu/nv/nw/nx/ny/nz/oa/ob/oc/od/oe/of/og/oh/oi/oj/ok/ol/om/on/oo/op/oq/or/os/ot/ou/ov/ow/ox/oy/oz/pa/pb/pc/pd/pe/pf/pg/ph/pi/pj/pk/pl/pm/pn/po/pp/pq/pr/ps/pt/pu/pv/pw/px/py/pz/qa/qb/qc/qd/qe/qf/qg/qh/qi/qj/qk/ql/qm/qn/qo/qp/qq/qr/qs/qt/qu/qv/qw/qx/qy/qz/ra/rb/rc/rd/re/rf/rg/rh/ri/rj/rk/rl/rm/rn/ro/rp/rq/rr/rs/rt/ru/rv/rw/rx/ry/rz/sa/sb/sc/sd/se/sf/sg/sh/si/sj/sk/sl/sm/sn/so/sp/sq/sr/ss/st/su/sv/sw/sx/sy/sz/ta/tb/tc/td/te/tf/tg/th/ti/tj/tk/tl/tm/tn/to/tp/tq/tr/ts/tt/tu/tv/tw/tx/ty/tz/ua/ub/uc/ud/ue/uf/ug/uh/ui/uj/uk/ul/um/un/uo/up/uq/ur/us/ut/uu/uv/uw/ux/uy/uz/va/vb/vc/vd/ve/vf/vg/vh/vi/vj/vk/vl/vm/vn/vo/vp/vq/vr/vs/vt/vu/vv/vw/vx/vy/vz/wa/wb/wc/wd/we/wf/wg/wh/wi/wj/wk/wl/wm/wn/wo/wp/wq/wr/ws/wt/wu/wv/ww/wx/wy/wz/xa/xb/xc/xd/xe/xf/xg/xh/xi/xj/xk/xl/xm/xn/xo/xp/xq/xr/xs/xt/xu/xv/xw/xw/xx/xy/xz/ya/yb/yc/yd/ye/yf/yg/yh/yi/yj/yk/yl/ym/yn/yo/yp/yq/yr/ys/yt/yu/yv/yw/yx/yy/yz/za/zb/zc/zd/ze/zf/zg/zh/zi/zj/zk/zl/zm/zn/zo/zp/zq/zr/zs/zt/zu/zv/zw/zx/zy/zz";
        String shortName = "test.txt";

        TarArchiveEntry entry = new TarArchiveEntry(shortName);
        entry.setSize(10);
        entry.setName(longFileName);

        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write("Hello, World!".getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.close();

        byte[] actualTarEntry = outputStream.toByteArray();

        int longLinkNameLength = longFileName.getBytes(StandardCharsets.UTF_8).length + 1;
        byte longLinkNameByte = (byte) (longLinkNameLength & 0xFF);
        byte longLinkNameBytes[] = longFileName.getBytes(StandardCharsets.UTF_8);

        byte expectedLongLinkNameByte = (byte) (longLinkNameLength + 1);
        byte expectedLongLinkNameBytes[] = new byte[longLinkNameLength + 1];
        System.arraycopy(longLinkNameBytes, 0, expectedLongLinkNameBytes, 0, longLinkNameLength);
        expectedLongLinkNameBytes[longLinkNameLength] = 0;

        Assert.assertEquals(expectedLongLinkNameByte, actualTarEntry[0], "Invalid long link name length.");
        Assert.assertArrayEquals(expectedLongLinkNameBytes, Arrays.copyOfRange(actualTarEntry, 1, 1 + longLinkNameLength), "Invalid long link name.");

        byte[] expectedTarEntryContent = new byte[1024];
        System.arraycopy(TarConstants.MAGIC, 0, expectedTarEntryContent, 0, TarConstants.MAGIC.length);
        TarArchiveEntry expectedEntry = new TarArchiveEntry(shortName);
        expectedEntry.setSize(10);
        expectedEntry.setName(longFileName);
        expectedEntry.writeEntryHeader(expectedTarEntryContent, 0);

        Assert.assertArrayEquals(expectedTarEntryContent, Arrays.copyOfRange(actualTarEntry, 0, 1 + TarConstants.BLOCKSIZE), "Invalid tar entry header.");

        ByteArrayInputStream longLinkEntryInputStream = new ByteArrayInputStream(actualTarEntry, 1 + TarConstants.BLOCKSIZE, longLinkNameLength + 1);
        TarArchiveEntry actualLongLinkEntry = new TarArchiveEntry((InputStream) longLinkEntryInputStream);

        Assert.assertEquals(longFileName, actualLongLinkEntry.getName(), "Invalid long link entry name.");

        byte[] actualData = new byte[actualEntry.getSize()];
        inputStream.read(actualData);

        Assert.assertArrayEquals(expectedData, actualData, "Invalid data written to the archive.");
    }
}