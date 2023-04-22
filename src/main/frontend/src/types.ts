import { RootType } from "./store";
import { OptionBarVariant } from "./util/optionbars";

export type FocusedObject = {
  id: string;
  type: string;
  optionbar: OptionBarVariant;
};

export type CodeEdition = {
  id: string;
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
  contextMenuCoordinates?: { left: number; top: number };
  isContextMenuActive: boolean;
};

export type StateType = {
  id: string;
  name: string;
  x: number;
  y: number;
  entryCode: string;
  exitCode: string;
};

export interface Trigger {
  id: string;
  name: string;
  parameters: { name: string; type: string }[];
  condition?: string;
  code: string;
}

export interface TransitionType extends Trigger {
  source: string;
  target: string;
}

export interface TriggerMapping extends Trigger {
  nextState: string;
}

export type DispatchType<T> = React.Dispatch<React.SetStateAction<T>>;
