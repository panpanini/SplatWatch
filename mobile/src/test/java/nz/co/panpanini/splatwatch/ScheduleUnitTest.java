package nz.co.panpanini.splatwatch;

import com.google.gson.JsonParser;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import nz.co.panpanini.datalayer.models.Block;
import nz.co.panpanini.datalayer.models.Schedule;


/**
 * Created by matthew <matthew@showgizmo.com> on 5/01/16.
 */
public class ScheduleUnitTest {

    private static final String json = "{\"updateTime\":1452103331975,\"schedule\":[{\"modes\":[{\"maps\":[{\"nameJP\":\"Ｂバスパーク\",\"nameEN\":\"Blackbelly Skatepark\",\"name\":{\"jp\":\"Ｂバスパーク\",\"en\":\"Blackbelly Skatepark\"}},{\"nameJP\":\"マヒマヒリゾート＆スパ\",\"nameEN\":\"Mahi-Mahi Resort\",\"name\":{\"jp\":\"マヒマヒリゾート＆スパ\",\"en\":\"Mahi-Mahi Resort\"}}],\"rules\":{\"jp\":\"ナワバリバトル\",\"en\":\"Turf War\"}},{\"maps\":[{\"nameJP\":\"デカライン高架下\",\"nameEN\":\"Urchin Underpass\",\"name\":{\"jp\":\"デカライン高架下\",\"en\":\"Urchin Underpass\"}},{\"nameJP\":\"ハコフグ倉庫\",\"nameEN\":\"Walleye Warehouse\",\"name\":{\"jp\":\"ハコフグ倉庫\",\"en\":\"Walleye Warehouse\"}}],\"rulesJP\":\"ガチエリア\",\"rulesEN\":\"Splat Zones\",\"rules\":{\"jp\":\"ガチエリア\",\"en\":\"Splat Zones\"}}],\"startTime\":1452103200000,\"endTime\":1452117600000,\"regular\":{\"maps\":[{\"nameJP\":\"Ｂバスパーク\",\"nameEN\":\"Blackbelly Skatepark\",\"name\":{\"jp\":\"Ｂバスパーク\",\"en\":\"Blackbelly Skatepark\"}},{\"nameJP\":\"マヒマヒリゾート＆スパ\",\"nameEN\":\"Mahi-Mahi Resort\",\"name\":{\"jp\":\"マヒマヒリゾート＆スパ\",\"en\":\"Mahi-Mahi Resort\"}}],\"rules\":{\"jp\":\"ナワバリバトル\",\"en\":\"Turf War\"}},\"ranked\":{\"maps\":[{\"nameJP\":\"デカライン高架下\",\"nameEN\":\"Urchin Underpass\",\"name\":{\"jp\":\"デカライン高架下\",\"en\":\"Urchin Underpass\"}},{\"nameJP\":\"ハコフグ倉庫\",\"nameEN\":\"Walleye Warehouse\",\"name\":{\"jp\":\"ハコフグ倉庫\",\"en\":\"Walleye Warehouse\"}}],\"rulesJP\":\"ガチエリア\",\"rulesEN\":\"Splat Zones\",\"rules\":{\"jp\":\"ガチエリア\",\"en\":\"Splat Zones\"}}},{\"modes\":[{\"maps\":[{\"nameJP\":\"シオノメ油田\",\"nameEN\":\"Saltspray Rig\",\"name\":{\"jp\":\"シオノメ油田\",\"en\":\"Saltspray Rig\"}},{\"nameJP\":\"マサバ海峡大橋\",\"nameEN\":\"Hammerhead Bridge\",\"name\":{\"jp\":\"マサバ海峡大橋\",\"en\":\"Hammerhead Bridge\"}}],\"rules\":{\"jp\":\"ナワバリバトル\",\"en\":\"Turf War\"}},{\"maps\":[{\"nameJP\":\"モズク農園\",\"nameEN\":\"Kelp Dome\",\"name\":{\"jp\":\"モズク農園\",\"en\":\"Kelp Dome\"}},{\"nameJP\":\"マヒマヒリゾート＆スパ\",\"nameEN\":\"Mahi-Mahi Resort\",\"name\":{\"jp\":\"マヒマヒリゾート＆スパ\",\"en\":\"Mahi-Mahi Resort\"}}],\"rulesJP\":\"ガチホコ\",\"rulesEN\":\"Rainmaker\",\"rules\":{\"jp\":\"ガチホコ\",\"en\":\"Rainmaker\"}}],\"startTime\":1452117600000,\"endTime\":1452132000000,\"regular\":{\"maps\":[{\"nameJP\":\"シオノメ油田\",\"nameEN\":\"Saltspray Rig\",\"name\":{\"jp\":\"シオノメ油田\",\"en\":\"Saltspray Rig\"}},{\"nameJP\":\"マサバ海峡大橋\",\"nameEN\":\"Hammerhead Bridge\",\"name\":{\"jp\":\"マサバ海峡大橋\",\"en\":\"Hammerhead Bridge\"}}],\"rules\":{\"jp\":\"ナワバリバトル\",\"en\":\"Turf War\"}},\"ranked\":{\"maps\":[{\"nameJP\":\"モズク農園\",\"nameEN\":\"Kelp Dome\",\"name\":{\"jp\":\"モズク農園\",\"en\":\"Kelp Dome\"}},{\"nameJP\":\"マヒマヒリゾート＆スパ\",\"nameEN\":\"Mahi-Mahi Resort\",\"name\":{\"jp\":\"マヒマヒリゾート＆スパ\",\"en\":\"Mahi-Mahi Resort\"}}],\"rulesJP\":\"ガチホコ\",\"rulesEN\":\"Rainmaker\",\"rules\":{\"jp\":\"ガチホコ\",\"en\":\"Rainmaker\"}}},{\"modes\":[{\"maps\":[{\"nameJP\":\"デカライン高架下\",\"nameEN\":\"Urchin Underpass\",\"name\":{\"jp\":\"デカライン高架下\",\"en\":\"Urchin Underpass\"}},{\"nameJP\":\"ネギトロ炭鉱\",\"nameEN\":\"Bluefin Depot\",\"name\":{\"jp\":\"ネギトロ炭鉱\",\"en\":\"Bluefin Depot\"}}],\"rules\":{\"jp\":\"ナワバリバトル\",\"en\":\"Turf War\"}},{\"maps\":[{\"nameJP\":\"ヒラメが丘団地\",\"nameEN\":\"Flounder Heights\",\"name\":{\"jp\":\"ヒラメが丘団地\",\"en\":\"Flounder Heights\"}},{\"nameJP\":\"マサバ海峡大橋\",\"nameEN\":\"Hammerhead Bridge\",\"name\":{\"jp\":\"マサバ海峡大橋\",\"en\":\"Hammerhead Bridge\"}}],\"rulesJP\":\"ガチヤグラ\",\"rulesEN\":\"Tower Control\",\"rules\":{\"jp\":\"ガチヤグラ\",\"en\":\"Tower Control\"}}],\"startTime\":1452132000000,\"endTime\":1452146400000,\"regular\":{\"maps\":[{\"nameJP\":\"デカライン高架下\",\"nameEN\":\"Urchin Underpass\",\"name\":{\"jp\":\"デカライン高架下\",\"en\":\"Urchin Underpass\"}},{\"nameJP\":\"ネギトロ炭鉱\",\"nameEN\":\"Bluefin Depot\",\"name\":{\"jp\":\"ネギトロ炭鉱\",\"en\":\"Bluefin Depot\"}}],\"rules\":{\"jp\":\"ナワバリバトル\",\"en\":\"Turf War\"}},\"ranked\":{\"maps\":[{\"nameJP\":\"ヒラメが丘団地\",\"nameEN\":\"Flounder Heights\",\"name\":{\"jp\":\"ヒラメが丘団地\",\"en\":\"Flounder Heights\"}},{\"nameJP\":\"マサバ海峡大橋\",\"nameEN\":\"Hammerhead Bridge\",\"name\":{\"jp\":\"マサバ海峡大橋\",\"en\":\"Hammerhead Bridge\"}}],\"rulesJP\":\"ガチヤグラ\",\"rulesEN\":\"Tower Control\",\"rules\":{\"jp\":\"ガチヤグラ\",\"en\":\"Tower Control\"}}}],\"splatfest\":false}";

    private static final String splatfest = "{\"updateTime\":1455948002251,\"schedule\":[{\"modes\":[{\"maps\":[{\"nameJP\":\"Ｂバスパーク\",\"nameEN\":\"Blackbelly Skatepark\",\"name\":{\"jp\":\"Ｂバスパーク\",\"en\":\"Blackbelly Skatepark\"}},{\"nameJP\":\"モンガラキャンプ場\",\"nameEN\":\"Camp Triggerfish\",\"name\":{\"jp\":\"モンガラキャンプ場\",\"en\":\"Camp Triggerfish\"}},{\"nameJP\":\"アンチョビットゲームズ\",\"nameEN\":\"Ancho-V Games\",\"name\":{\"jp\":\"アンチョビットゲームズ\",\"en\":\"Ancho-V Games\"}}],\"rules\":{\"jp\":\"フェス\",\"en\":\"Splatfest\"},\"teams\":[\"Pokémon Red\",\"Pokémon Blue\"]}],\"startTime\":1455948000000,\"endTime\":1456034400000,\"regular\":{\"maps\":[{\"nameJP\":\"Ｂバスパーク\",\"nameEN\":\"Blackbelly Skatepark\",\"name\":{\"jp\":\"Ｂバスパーク\",\"en\":\"Blackbelly Skatepark\"}},{\"nameJP\":\"モンガラキャンプ場\",\"nameEN\":\"Camp Triggerfish\",\"name\":{\"jp\":\"モンガラキャンプ場\",\"en\":\"Camp Triggerfish\"}},{\"nameJP\":\"アンチョビットゲームズ\",\"nameEN\":\"Ancho-V Games\",\"name\":{\"jp\":\"アンチョビットゲームズ\",\"en\":\"Ancho-V Games\"}}],\"rules\":{\"jp\":\"フェス\",\"en\":\"Splatfest\"},\"teams\":[\"Pokémon Red\",\"Pokémon Blue\"]}}],\"splatfest\":true}";

    Schedule schedule;

    @Before
    public void setup(){
        JsonParser parser = new JsonParser();
        schedule = Schedule.fromJson(parser.parse(json).getAsJsonObject());
    }

    @After
    public void teardown(){
        schedule = null;
    }

    @Test
    public void testScheduleParsing(){

        Assert.assertEquals("There should only ever be 3 schedule blocks", schedule.getBlocks().size(), 3);
        Assert.assertEquals("Splatfest is not currently running", schedule.isSplatfest(), false);
    }


    @Test
    public void testRegularMapParsing(){
        Block schedule = this.schedule.getBlocks().get(0);

        Assert.assertEquals("Name does not match", schedule.getRegularMaps().getMaps().get(0).getNameEN(), "Blackbelly Skatepark");
        Assert.assertEquals("Name does not match", schedule.getRegularMaps().getMaps().get(1).getNameEN(), "Mahi-Mahi Resort");

        Assert.assertEquals("Rules do not match", schedule.getRegularMaps().getRulesEN(), "Turf War");

        Assert.assertEquals("Start time does not match", schedule.getStartTime(), 1452103200000L);
        Assert.assertEquals("End time does not match", schedule.getEndTime(), 1452117600000L);

    }

    @Test
    public void testRankedMapParsing(){
        Block schedule = this.schedule.getBlocks().get(0);

        Assert.assertEquals("Name does not match", schedule.getRankedMaps().getMaps().get(0).getNameEN(), "Urchin Underpass");
        Assert.assertEquals("Name does not match", schedule.getRankedMaps().getMaps().get(1).getNameEN(), "Walleye Warehouse");

        Assert.assertEquals("Rules do not match", schedule.getRankedMaps().getRulesEN(), "Splat Zones");

        Assert.assertEquals("Start time does not match", schedule.getStartTime(), 1452103200000L);
        Assert.assertEquals("End time does not match", schedule.getEndTime(), 1452117600000L);
    }

}
