import React, { useState, ChangeEvent, useEffect } from 'react';
import styled from 'styled-components';
import NumberFilter from './NumberFilter';
import ActivityLevel from './ActivityLevel';
import TimeFilter from './TimeFilter';
import DistanceFilter from './DistanceFilter';
import { TextField } from '@material-ui/core';
import ViewBox from './ViewBox';
import TagTextField from './TagTextField';
import ActivityLevels from '../../interfaces/ActivityLevels';

const StyledContainer = styled.div`
    margin-left: 1rem;
    margin-top: 1rem;
    width: 95%;
`;

interface Props {
    onTitleSearch: (searchResult: string) => void;
    onTimeFromChange: (searchResult: Date) => void;
    onTimeToChange: (searchResult: Date) => void;
    onLevelChange: (levels: ActivityLevels) => void;
    onShowFuture: (state: boolean) => void;
    onShowMine: (state: boolean) => void;
    onCapacityChange: (range: number[]) => void;
}

const SideFilter = ({
    onTitleSearch,
    onShowFuture,
    onShowMine,
    onCapacityChange,
    onLevelChange,
    onTimeFromChange,
    onTimeToChange,
}: Props) => {
    const [titleSearch, setTitleSearch] = useState<string>('');

    const onChangeTitle = (event: ChangeEvent<HTMLInputElement>) => {
        const current: string = (event.target as HTMLInputElement).value;
        setTitleSearch(current);
    };

    useEffect(() => {
        onTitleSearch(titleSearch);
    }, [titleSearch]);

    return (
        <StyledContainer>
            <TextField
                onChange={onChangeTitle}
                fullWidth={true}
                label="Søk på tittel"
                variant="outlined"
            />
            <ViewBox
                label={'Vis kun framtidige aktiviteter'}
                onStateChange={onShowFuture}
            ></ViewBox>
            <ViewBox
                label={'Vis kun påmeldte aktiviteter'}
                onStateChange={onShowMine}
            ></ViewBox>
            <DistanceFilter></DistanceFilter>
            <TimeFilter
                onTimeFromChange={onTimeFromChange}
                onTimeToChange={onTimeToChange}
            ></TimeFilter>
            <NumberFilter
                minValue={0}
                maxValue={20}
                headline={'Kapasitet'}
                onCapacityChange={onCapacityChange}
            ></NumberFilter>
            <TagTextField></TagTextField>
            <ActivityLevel onLevelChange={onLevelChange}></ActivityLevel>
        </StyledContainer>
    );
};

export default SideFilter;
