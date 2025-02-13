---
layout: page
title: Developer Guide
---
* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* {list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams in this document can be found in the [diagrams](https://github.com/AY2122S1-CS2103T-T15-2/tp/tree/master/docs/diagrams/) folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

### Architecture

<img src="images/ArchitectureDiagram.png" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** has two classes called [`Main`](https://github.com/AY2122S1-CS2103T-T15-2/tp/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/AY2122S1-CS2103T-T15-2/tp/tree/master/src/main/java/seedu/address/MainApp.java). It is responsible for,
* At app launch: Initializes the components in the correct sequence, and connects them up with each other.
* At shut down: Shuts down the components and invokes cleanup methods where necessary.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

The rest of the App consists of four components.

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.


**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/AY2122S1-CS2103T-T15-2/tp/tree/master/src/main/java/seedu/address/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `MemberListPanel`, 
`EventListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` 
class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files 
that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.
com/AY2122S1-CS2103T-T15-2/tp/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in 
[`MainWindow.fxml`](https://github.com/AY2122S1-CS2103T-T15-2/tp/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Member` and `Event` object residing in the `Model`.

#### Current Implementations 

The GUI currently reflects the entered events and members recorded in Ailurus. Currently, there are two main windows 
that reflect the `Event` and `Member` objects that are residing in the `Model`. Directly adding or removing `Event` 
or `Member` would update the `EventListPanel` and `MemberListPanel` to show their respective `EventCard` 
and `MemberCard` accordingly. Each of the `EventCard` and `MemberCard` would display the fields under the 
corresponding `Event` and `Member` objects as discussed under [Model Component](#model-component).

However, there are problems faced when the fields inside `Event` and `Member` are being changed. There seems to be 
some difficulty in updating the `MemberCard` when a `Task` object is being created under the `Member` object, or is 
removed. Similarly, the same problem also lies in `EventCard` not updating when a `Member` object associated with 
the `Event` object is being removed.

#### Future Plans

To address the above-mentioned bug where the `EventCard` and `MemberCard` are not updated spontaneously, we decided 
to implement a third column featuring `Task` objects. As such, we are able to totally remove the `Member` and `Task` 
from `EventCard` and `MemberCard` respectively. 

We plan to support this implementation by using the `elist`, `mlist` and `tlist` commands to determine what is being 
displayed in the `MainWindow`.

### Logic component

**API** : [`Logic.java`](https://github.com/AY2122S1-CS2103T-T15-2/tp/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="550"/>

How the `Logic` component works:
1. When `Logic` is called upon to execute a command, it uses the `AddressBookParser` class to parse the user command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `MaddCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to add a member).
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

The Sequence Diagram below illustrates the interactions within the `Logic` component for the `execute("delete 1")` API call.

![Interactions Inside the Logic Component for the `delete 1` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.
</div>

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

Here is the Activity Diagram for a User when choosing the module and command to interact with in Ailurus:

![Activity Diagram for User Commands](images/CommandActivityDiagram.jpg)

#### Current Implementation

New feature: Events
* Events can be added and deleted from event list via `eadd` and `edelete` commands
* The participating members can be listed using the command `mlist /v EVENT_ID`
* New events created can have many participants selected from member list.
* <u>Design Decision</u>: Instead of only allowing adding of events and creating a command
for adding participants separately, eadd command allows creation of complete event to
minimise commands required to add them individually. The format is similar to `delete` and `list` commands
for familiarity with similar commands for other modules.


#### Future Plans

Future plans for Events
* Include adding and deleting of participants, as well as marking whether a participant has attended the event.
* Include updating of event with participants and different name
* Include searching for the list of events for a participant
* Include filtering of events by month or events that are happening today.
* Include sorting of events by date, name or number of participants.
  * Dates should be in reverse chronological order so that upcoming events are shown first
* Include additional remarks or description for an event

### Add a task feature for a member or several members

#### Current Implementation

The proposed feature is achieved by getting the member(s) from the filtered member list
and use API from the model manager to add the task with given task name to each of the members.

The operations are exposed in the `Model` interface as `Model#getFilteredMemberlist()` and `Model#addTask()`.

Given below is an example usage scenario:

The user executes `tadd /n take attendance /m 1 /m 2`. The parser will be called upon to create a TaddCommandParser.
The parser will then parse the input to create a TaddCommand with task name as "take attendance" and member ids 1 and 2.
This command will add the task "take attendance" to the first and second member of the member list.

### Delete a task feature for a member

#### Current Implementation

The proposed feature is achieved by getting the member(s) from the filtered member list
and use API from the model manager to delete the task with given task id from the member with given member id.

The operations are exposed in the `Model` interface as `Model#getFilteredMemberlist()` and `Model#deleteTask()`.

Given below is an example usage scenario:

The user executes `tdel /t 1 /m 1`. The parser will be called upon to create a TdelCommandParser.
The parser will then parse the input to create a TdelCommand with task id as 1 and member id as 1.
This command will delete the first task from the task list of the first member of the member list.

### List tasks feature for a member

#### Current Implementation

The proposed feature is achieved by getting the member with given member id from the filtered member list
and use API from the model manager to list all the tasks of the member.

The operations are exposed in the `Model` interface as `Model#getFilteredMemberlist()` and `Model#updateFilteredTaskList()`.

Given below is an example usage scenario:

The user executes `tlist /m 1`. The parser will be called upon to create a TlistCommandParser.
The parser will then parse the input to create a TlistCommand with member id as 1.
This command will display all the tasks of the first member of the member list.
=======

### Model component
**API** : [`Model.java`](https://github.com/AY2122S1-CS2103T-T15-2/tp/tree/master/src/main/java/seedu/address/model/Model.java)

<img src="images/ModelClassDiagram.png" width="450" />


The `Model` component,

* stores the address book data i.e., all `Member` objects (which are contained in a `UniqueMemberList` object), all `Event` objects (which are contained in a `UniqueEventList` object).
* stores a `TaskList` reference that points to the `TaskList` object that contains all the `Task` objects of the currently 'selected' `Member` object (e.g. result of a 'tlist' command)
* stores the currently 'selected' `Member`, `Event` and `Task` objects (e.g., results of a search query) as separate _filtered_ lists which are exposed to outsiders as an unmodifiable `ObservableList<Member>`, `ObservableList<Event>` and `ObservableList<Task>` respectively that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<div markdown="span" class="alert alert-info">:information_source: **Note:** An alternative (arguably, a more OOP) model is given below. It has a `Position` list in the `AddressBook`, which `Member` references. This allows `AddressBook` to only require one `Position` object per unique POSITION, instead of each `Member` needing their own set of `Position` objects.<br>


<img src="images/BetterModelClassDiagram.png" width="450" />

</div>

#### Current Implementations

The model that we implemented currently has `Event`, `Task` and `Member`. `Member` has a field with `TaskList` which contains
`Task` belonging to the `Member`. `Event` has a `Name` field, `EventDate` field, and a field of a HashMap<Member, Boolean>
to serve as a participant list with attendance. Event and Member both extend from the abstract class `Module` to reduce class duplication.

The `Task` model we implemented currently has a task name and a state that represents it is done or not.

#### Future Plans

The future plan for the model is to have `Task` extend from module. The search functions in regard to name will be greatly helped ny the `Module` class.
We also plan to make the `Position` objects unique to reduce space cost, Each member would contain a reference to the `Position` object instead.

* Make `Task` a subclass of `Module`, which involves adding `TaskName` class.
* Add deadline field to `Task`


### Storage component

**API** : [`Storage.java`](https://github.com/AY2122S1-CS2103T-T15-2/tp/tree/master/src/main/java/seedu/address/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,
* can save both address book data and user preference data in json format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

#### Current Implementation

`JsonAdaptedTask` allows `Task` to be stored in a json format and `JsonAdaptedMember` allows `Member` to store an array of `Task`

`JsonAdaptedEvent` allows `Event` to be stored in Json format. Ailurus can now store `Event`, enabling the saving and 
loading of files with `Event` objects. The Map of participants of the `Event` are saved into Json format by splitting them into two separate lists of `JsonAdaptedMember` and `Boolean` respectively.

#### Future Plans

Storing `Position` in a unique list would reduce the amount of `Position` objects needed.

### Common classes

Classes used by multiple components are in the `seedu.addressbook.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

![UndoRedoState0](images/UndoRedoState0.png)

Step 2. The user executes `delete 5` command to delete the 5th member in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

![UndoRedoState1](images/UndoRedoState1.png)

Step 3. The user executes `madd /n David …​` to add a new member. The `madd` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

![UndoRedoState2](images/UndoRedoState2.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</div>

Step 4. The user now decides that adding the member was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

![UndoRedoState3](images/UndoRedoState3.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</div>

The following sequence diagram shows how the undo operation works:

![UndoSequenceDiagram](images/UndoSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</div>

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</div>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

![UndoRedoState4](images/UndoRedoState4.png)

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add /n David …​` command. This is the behavior that most modern desktop applications follow.

![UndoRedoState5](images/UndoRedoState5.png)

The following activity diagram summarizes what happens when a user executes a new command:

<img src="images/CommitActivityDiagram.png" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the member being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* prefer desktop apps over other types
* has trouble managing the multitude of details related to the members of his club
* want to assign tasks for members
* has a need to organize and plan events for members
* likes typing and comfortable with CLI
* prefers typing to mouse interactions

**Value proposition**: 

* manage contacts faster than a typical mouse/GUI driven app
* manage club events and tasks/activities for large amount of members
* contact and personal information of members collated in an easily accessible location
* able to update details relating to members
* categorise members into groups for smoother planning


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

#### Member-related Functions

| Priority | As a …​                                    | I want to …​                     | So that I can…​                                                        |
| -------- | ------------------------------------------ | ------------------------------ | ---------------------------------------------------------------------- |
| `* * *`  | user                                       | add a new member               | update the increase or change in members |
| `* * *`  | user                                       | have address fields for members | |
| `* * *`  | user                                       | kick a member                | remove members or troublemakers from the club |
| `* * *`  | user                                       | have email address field for members | |

#### Task Functions

| Priority | As a …​                                    | I want to …​                     | So that I can…​                                                        |
| -------- | ------------------------------------------ | ------------------------------ | ---------------------------------------------------------------------- |
| `* * *`  | user                                       | create tasks for my members or myself | |
| `* *`  | user                                       | see the completion status and description of tasks for members | know the requirements and status of the task |
| `*`  | user                                       | mark a task as completed, overdue or uncompleted | keep track of my tasks that are on-hand |
| `* *`  | user                                       | add a deadline to task | keep track of who has overdue tasks |
| `* * *`  | user                                       | delete already obscure and unnecessary tasks | have a cleaner task list |


#### Storage Functions
| Priority | As a …​                                    | I want to …​                     | So that I can…​                                                        |
| -------- | ------------------------------------------ | ------------------------------ | ---------------------------------------------------------------------- |
| `* * *`  | user                                       | load members from other files | access and manage different sets of data |
| `* * *`  | user                                       | write my data to a file as save data | access them and resume at a later date |

#### Event Functions
| Priority | As a …​                                    | I want to …​                     | So that I can…​                                                        |
| -------- | ------------------------------------------ | ------------------------------ | ---------------------------------------------------------------------- |
| `* * *`  | user                                       | add all members of a particular event to one group | send notifications to only those involved |


#### Other miscellaneous Functions
| Priority | As a …​                                    | I want to …​                     | So that I can…​                                                        |
| -------- | ------------------------------------------ | ------------------------------ | ---------------------------------------------------------------------- |
| `* * *`  | new user                                   | see usage instructions         | refer to instructions when I forget how to use the App                 |
| `* * *`  | user                                       | find a member by name          | locate details of members without having to go through the entire list |
| `* *`    | user                                       | hide private contact details   | minimize chance of someone else seeing them by accident                |
| `*`      | user with many members in the address book | sort members by name           | locate a member easily                                                 |

*{More to be added}*

### Use cases

(For all use cases below, the **System** is the `Ailurus` and the **Actor** is the `user`, unless specified otherwise)

**Use case: UC1 - Add a member**

**MSS**

1.  User requests to add a member, providing necessary details.
2.  Ailurus adds the member.

    Use case ends.

**Extensions**

* 1a. Invalid format or incomplete details provided by user

    * 1a1. Ailurus shows an error message about missing or invalid input.

      Use case ends.

**Use case: UC2 - Delete a member**

**MSS**

1.  User requests to list members
2.  Ailurus shows a list of members
3.  User requests to delete a specific member in the list
4.  Ailurus deletes the member

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given index is invalid.

    * 3a1. Ailurus shows an error message.

      Use case resumes at step 2.

**Use case: UC3 - Add a task to a member**

**MSS**

1.  User requests to add a task to a specific member, providing details if necessary.
2.  Ailurus adds the task to the member.

    Use case ends.

**Extensions**

* 1a. Invalid format or incomplete details provided by user

    * 1a1. Ailurus shows an error message about missing or invalid input.

      Use case ends.

* 1b. The given index is invalid.

    * 1b1. Ailurus shows an error message about invalid index.

      Use case ends.

**Use case: UC4 - Delete a task from a member**

**MSS**

1.  User requests to list tasks of a specific member
2.  Ailurus shows a list of tasks
3.  User requests to delete a specific task in the list
4.  Ailurus deletes the task

    Use case ends.

**Extensions**

* 1a. The given index of member is invalid

    * 1a1. Ailurus shows an error message.

      Use case ends.

* 2a. The list is empty.

  Use case ends.

* 3a. The given index of task is invalid.

    * 3a1. Ailurus shows an error message.

      Use case resumes at step 2.

**Use case: UC5 - Mark a task as done**

**MSS**

1.  User requests to list tasks of a specific member
2.  Ailurus shows a list of tasks
3.  User requests to mark a specific task as done
4.  Ailurus marks task as done

    Use case ends.

**Extensions**

* 1a. The given index of member is invalid

    * 1a1. Ailurus shows an error message.

      Use case ends.

* 2a. The list is empty.

  Use case ends.

* 3a. The given index of task is invalid.

    * 3a1. Ailurus shows an error message.

      Use case resumes at step 2.

**Use case: UC6 - Add an event**

**MSS**

1.  User requests to add an event, providing necessary details.
2.  Ailurus adds the event.

    Use case ends.

**Extensions**

* 1a. Invalid format or incomplete details provided by user

    * 1a1. Ailurus shows an error message about missing or invalid input.

      Use case ends.

**Use case: UC7 - Delete an event**

**MSS**

1.  User requests to list events
2.  Ailurus shows a list of events
3.  User requests to delete a specific event in the list
4.  Ailurus deletes the event

    Use case ends.

**Extensions**

* 1a. Invalid format or incomplete details provided by user

    * 1a1. Ailurus shows an error message about missing or invalid input.

      Use case ends.

*{More to be added}*

### Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `11` or above installed.
2.  Should be able to hold up to 1000 members without a noticeable sluggishness in performance for typical usage.
3.  A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4.  Should run on user computer with double-click - no installer or additional libraries required.
5.  The system should respond within two seconds.
6.  The system should work on both 32-bit and 64-bit environments.
7.  The system should be usable by a novice who has never used a CLI app before.
8.  The project is expected to adhere to a schedule that delivers features of a milestone for every two weeks.    
9.  Should be able to hold up to 1000 event managers and participants without a noticeable delay (less than 2 seconds) in performance for typical usage.
10. The product should be for a single user i.e. (not a multi-user product), and should not depend on a remote server and does not require an installer. 
11. The software should work on the Windows, Linux, and OS-X platforms.
12. The GUI should work well with standard screen resolutions 1920x1080 and higher, and
    for screen scales 100% and 125%. It should be usable for resolutions 1280x720 and higher, and
    for screen scales 150%.    
    *{More to be added}*

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, OS-X
* **Private contact detail**: A contact detail that is not meant to be shared with others

*{More to be added}*

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a member

1. Deleting a member while all members are being shown

   1. Prerequisites: List all members using the `list` command. Multiple members in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No member is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
