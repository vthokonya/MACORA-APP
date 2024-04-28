package zw.co.tayasoft.views.wards;

import com.vaadin.collaborationengine.CollaborationAvatarGroup;
import com.vaadin.collaborationengine.CollaborationBinder;
import com.vaadin.collaborationengine.UserInfo;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import zw.co.tayasoft.data.Ward;
import zw.co.tayasoft.services.WardService;
import zw.co.tayasoft.views.MainLayout;

@PageTitle("Wards")
@Route(value = "wards/:wardID?/:action?(edit)", layout = MainLayout.class)
@AnonymousAllowed
public class WardsView extends Div implements BeforeEnterObserver {

    private final String WARD_ID = "wardID";
    private final String WARD_EDIT_ROUTE_TEMPLATE = "wards/%s/edit";

    private final Grid<Ward> grid = new Grid<>(Ward.class, false);

    CollaborationAvatarGroup avatarGroup;

    private TextField name;
    private TextField councellor;
    private TextField mobileNumber1;
    private TextField mobileNumber2;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");

    private final CollaborationBinder<Ward> binder;

    private Ward ward;

    private final WardService wardService;

    public WardsView(WardService wardService) {
        this.wardService = wardService;
        addClassNames("wards-view");

        // UserInfo is used by Collaboration Engine and is used to share details
        // of users to each other to able collaboration. Replace this with
        // information about the actual user that is logged, providing a user
        // identifier, and the user's real name. You can also provide the users
        // avatar by passing an url to the image as a third parameter, or by
        // configuring an `ImageProvider` to `avatarGroup`.
        UserInfo userInfo = new UserInfo(UUID.randomUUID().toString(), "Steve Lange");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        avatarGroup = new CollaborationAvatarGroup(userInfo, null);
        avatarGroup.getStyle().set("visibility", "hidden");

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("name").setAutoWidth(true);
        grid.addColumn("councellor").setAutoWidth(true);
        grid.addColumn("mobileNumber1").setAutoWidth(true);
        grid.addColumn("mobileNumber2").setAutoWidth(true);
        grid.setItems(query -> wardService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(WARD_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(WardsView.class);
            }
        });

        // Configure Form
        binder = new CollaborationBinder<>(Ward.class, userInfo);

        // Bind fields. This is where you'd define e.g. validation rules

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.ward == null) {
                    this.ward = new Ward();
                }
                binder.writeBean(this.ward);
                wardService.update(this.ward);
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(WardsView.class);
            } catch (ObjectOptimisticLockingFailureException exception) {
                Notification n = Notification.show(
                        "Error updating the data. Somebody else has updated the record while you were making changes.");
                n.setPosition(Position.MIDDLE);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (ValidationException validationException) {
                Notification.show("Failed to update the data. Check again that all values are valid");
            }
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> wardId = event.getRouteParameters().get(WARD_ID).map(Long::parseLong);
        if (wardId.isPresent()) {
            Optional<Ward> wardFromBackend = wardService.get(wardId.get());
            if (wardFromBackend.isPresent()) {
                populateForm(wardFromBackend.get());
            } else {
                Notification.show(String.format("The requested ward was not found, ID = %d", wardId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(WardsView.class);
            }
        }
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        name = new TextField("Name");
        councellor = new TextField("Councellor");
        mobileNumber1 = new TextField("Mobile Number1");
        mobileNumber2 = new TextField("Mobile Number2");
        formLayout.add(name, councellor, mobileNumber1, mobileNumber2);

        editorDiv.add(avatarGroup, formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Ward value) {
        this.ward = value;
        String topic = null;
        if (this.ward != null && this.ward.getId() != null) {
            topic = "ward/" + this.ward.getId();
            avatarGroup.getStyle().set("visibility", "visible");
        } else {
            avatarGroup.getStyle().set("visibility", "hidden");
        }
        binder.setTopic(topic, () -> this.ward);
        avatarGroup.setTopic(topic);

    }
}
