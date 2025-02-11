package pl.sda.scrum;

import com.google.common.collect.ImmutableList;
import pl.sda.task.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Sprint {
    private List<SprintItem> sprintItems;

    private boolean confirmed;

    public Sprint() {
        sprintItems = new ArrayList<>();
    }

    public void commitBacklogItem(BacklogItem item) {
        SprintItem sprintItem = new SprintItem(item.getId(), item.getTitle(), item.getDescription());
        sprintItems.add(sprintItem);
    }

    public List<SprintItem> commitedItems() {
        return ImmutableList.copyOf(sprintItems);
    }

    public void confirm() throws CannotConfirmSprintException {
        for (SprintItem sprintItem: sprintItems
             ) {
            if (!sprintItem.assignedUser().isPresent()){
                throw new CannotConfirmSprintException();
            }
            confirmed = true;
        }



    }

    public void assignItemToUser(Long itemId, User user) {
        sprintItems.stream().filter(sprintItem -> sprintItem.getId().equals(itemId))
                .findAny().ifPresent(sprintItem -> {
            sprintItem.assignToUser(user);
        });
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void markItemAsFinished(Long itemId, User owner) {
        sprintItems.stream().filter(sprintItem -> sprintItem.getId().equals(itemId))
                .findAny().ifPresent(SprintItem::finish);
    }

    public boolean isDone() {
        return sprintItems.stream().allMatch(SprintItem::isFinished);
    }

    public List<SprintItem> finishedItems() {
        return sprintItems.stream().filter(SprintItem::isFinished).collect(Collectors.toList());
    }
}
