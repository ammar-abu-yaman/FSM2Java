import { RootType } from "../store";

export interface FsmJson {
  options: any;
  states: {
    name: string;
    enterCode: string;
    exitCode: string;
    transitions: {
      trigger: {
        name: string;
        params: { name: string; type: string }[];
      };
      guard?: string;
      nextState: string;
      code: string;
    }[];
  }[];
}

export function aggregateState(data: RootType): FsmJson {
  const { metaData } = data;
  const options: any = {
    class: [metaData.className],
    package: [metaData.package],
    "initial-state": [metaData.initialState ?? ""],
    actions: metaData.actions,
  };

  const states = data.states.map((state) => ({
    name: state.name,
    enterCode: state.entryCode,
    exitCode: state.exitCode,
    transitions: data.transitions
      .filter((transition) => transition.source === state.id)
      .map((transition) => ({
        trigger: {
          name: transition.name,
          params: transition.parameters,
        },
        guard: transition.condition,
        nextState: data.states.find((state) => state.id === transition.target)
          ?.name,
        code: transition.code,
      })),
  }));

  return { options, states };
}
