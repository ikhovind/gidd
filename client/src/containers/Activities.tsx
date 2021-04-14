import React, {
    ChangeEvent,
    KeyboardEventHandler,
    useEffect,
    useState,
} from 'react';
import ActivityPopup from '../components/CreateActivity/ActivityPopup';
import ActivityForm from '../components/CreateActivity/ActivityForm';
import styled from 'styled-components';
import SideFilter from '../components/Filters/SideFilter';
import SortMenu from '../components/Sorting/SortMenu';
import Activity, { ActivityList } from '../interfaces/Activity';
import ActivityGrid from '../components/ActivityComponents/ActivityGrid';
import AddButton from '../components/AddButton';

//Endringer kan forekomme her

const Container = styled.div`
    display: flex;
    margin-left: 10px;
    width: 100%;
`;

const AddAndSort = styled.div`
    display: flex;
    justify-content: space-between;
`;

const View = styled.div`
    display: flex;
    flex-direction: column;
    justify-content: center;
    width: 75%;
    margin-left: 3rem;
    margin-top: 10px;
    margin-right: 3rem;
`;

const Activities = () => {
    const [activities, setActivities] = useState<Activity[]>([]);
    const [openPopup, setOpenPopup] = useState<boolean>(false);

    const onClickAddButton = () => {
        setOpenPopup(!openPopup);
    };

    useEffect(() => {
        const Liste = ActivityList();
        //Kode for å hente ut alle aktiviteter
        setActivities(Liste);
    }, []);

    return (
        <Container>
            <SideFilter></SideFilter>
            <View>
                <AddAndSort>
                    <SortMenu></SortMenu>
                    <AddButton onClick={onClickAddButton}></AddButton>
                    <ActivityPopup
                        title="Legg til aktivitet"
                        openPopup={openPopup}
                        setOpenPopup={setOpenPopup}
                    >
                        <ActivityForm
                            openPopup={openPopup}
                            setOpenPopup={setOpenPopup}
                        />
                    </ActivityPopup>
                </AddAndSort>
                <ActivityGrid activities={activities}></ActivityGrid>
            </View>
        </Container>
    );
};

export default Activities;
