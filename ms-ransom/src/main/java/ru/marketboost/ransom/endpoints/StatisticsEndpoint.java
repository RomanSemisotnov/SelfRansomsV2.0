package ru.marketboost.ransom.endpoints;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.marketboost.ransom.models.Stat;
import ru.marketboost.ransom.models.WbReplacing;
import ru.marketboost.ransom.services.WbSearchGoodsCountService;
import ru.marketboost.ransom.services.YandexWordStatService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class StatisticsEndpoint {

    @Autowired
    private WbSearchGoodsCountService wbSearchGoodsCountService;

    @Autowired
    private YandexWordStatService yandexWordStatService;

    @GetMapping(value = "/check")
    public Set<String> check() throws IOException {
        Set<String> newStat = loadObjectList("new_stat.csv").keySet();
        Set<String> stat = loadObjectList("stat.csv").keySet();
        Map<String, String> replacingMap = getReplacingMap();

        return stat.stream().filter(el -> !newStat.contains(el) &&
                (replacingMap.containsKey(el) && !newStat.contains(replacingMap.get(el)))
        ).collect(Collectors.toSet());
    }

    @GetMapping(value = "/stat/yandex", produces = "application/json")
    public Map<String, Stat> getYa() throws IOException {
        Map<String, Stat> finalCsv = loadObjectList("new_stat.csv");

        yandexWordStatService.parseStat(new ArrayList<>(finalCsv.values()));

        return finalCsv;
    }

    @GetMapping(value = "/stat/wb", produces = "application/json")
    public String get() throws IOException {
        Map<String, String> replacingMap = getReplacingMap();

        Map<String, Stat> finalCsv = loadObjectList("new_stat.csv");

        Map<String, Stat> initialCsv = loadObjectList("stat.csv")
                .entrySet()
                .stream()
                .collect(Collectors.toMap(ent -> {
                            if (replacingMap.containsKey(ent.getKey())) {
                                return replacingMap.get(ent.getKey());
                            }
                            return ent.getKey();
                        }, ent -> {
                            if (replacingMap.containsKey(ent.getKey())) {
                                ent.getValue().setName(replacingMap.get(ent.getKey()));
                            }
                            return ent.getValue();
                        }
                ))
                .entrySet()
                .stream()
                .filter(ent -> {
                    if (replacingMap.containsKey(ent.getKey())) {
                        return !finalCsv.containsKey(replacingMap.get(ent.getKey())) || finalCsv.get(replacingMap.get(ent.getKey())).getIsNeedToRecheck();
                    } else {
                        return !finalCsv.containsKey(ent.getKey()) || finalCsv.get(ent.getKey()).getIsNeedToRecheck();
                    }
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        wbSearchGoodsCountService.getStats(
                initialCsv.keySet(),
                finalCsv.values().stream().filter(obj -> !obj.getIsNeedToRecheck()).collect(Collectors.toList()),
                replacingMap
        );
        return "ok";
    }

    public Map<String, Stat> loadObjectList(String fileName) throws IOException {
        CsvSchema bootstrapSchema = CsvSchema.emptySchema().withHeader();
        CsvMapper mapper = new CsvMapper();
        File file = new File(fileName);
        MappingIterator<Stat> readValues =
                mapper.reader(Stat.class).with(bootstrapSchema).readValues(file);
        return readValues.readAll()
                .stream()
                .collect(Collectors.groupingBy(Stat::getName))
                .entrySet()
                .stream()
                .filter(entry -> !entry.getKey().isBlank())
                .collect(Collectors.toMap(e -> e.getKey().trim(), entry -> entry.getValue().get(0)));
    }

    public Map<String, String> getReplacingMap() throws IOException {
        CsvSchema bootstrapSchema = CsvSchema.emptySchema().withHeader();
        CsvMapper mapper = new CsvMapper();
        File file = new File("from2to.csv");
        MappingIterator<WbReplacing> readValues =
                mapper.reader(WbReplacing.class).with(bootstrapSchema).readValues(file);

        return readValues.readAll()
                .stream()
                .filter(replacing -> !replacing.getFrom().isBlank() && !replacing.getTo().isBlank())
                .collect(Collectors.toMap(WbReplacing::getFrom, WbReplacing::getTo));
    }


}
