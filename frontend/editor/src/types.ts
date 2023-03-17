import { RootType } from "./store";

export type FocusedObject = {
  id: number;
  type: string;
  optionBarContent: () => JSX.Element;
};

export type CodeEdition = {
  id: number;
  type: string;
  label: string;
  getValue: (state: RootType) => string;
  onChange: (newValue: string) => void;
};

export type MetaData = {
  package: string;
  className: string;
  initialState?: string;
  actions: string[];
};

export type StateType = {
  id: number;
  name: string;
  x: number;
  y: number;
  entryCode: string;
  exitCode: string;
};

export interface Trigger {
  id: number;
  name: string;
  parameters: { name: string; type: string }[];
  condition?: string;
  code: string;
}

export interface TransitionType extends Trigger {
  sourceId: { stateId: number; anchor: number };
  destId: { stateId: number; anchor: number };
}

export interface TriggerMapping extends Trigger {
  nextState: string;
}

export type DispatchType<T> = React.Dispatch<React.SetStateAction<T>>;
