import pandas as pd
import matplotlib.pyplot as plt

def plot_line(data, label, y="time"):
    plt.plot(data["matrix_size"], data[y], label=label, marker='o')

def finish_plot(title):
    plt.xlabel("Matrix Size")
    plt.ylabel("Time (s)")
    plt.title(title)

    plt.legend()

    plt.grid(True)
    plt.savefig(f"{title}.png")
    plt.clf()

def plot_line_vs_block(y="time"):
    block_df = pd.read_csv('block_data.csv')
    line_df = pd.read_csv('line_data_compare_block.csv')

    for size in block_df["block_size"].unique():
        data_subset = block_df[block_df["block_size"] == size]
        plot_line(data_subset, f"Block size: {size}")

    plot_line(line_df, label="Line", y=y)    # create a line for the line multiplication algorithm
    finish_plot(f"Block vs Line Matrix Multiplication ({y})")

def plot_naive_vs_line(y="time"):
    naive_df = pd.read_csv('naive_data.csv')
    line_df = pd.read_csv('line_data.csv')

    plot_line(naive_df, label="Naive", y=y)
    plot_line(line_df, label="Line", y=y)

    finish_plot(f"Naive vs Line Matrix Multiplication ({y})")

def plot_L1_L2_vs_time():
    naive_df = pd.read_csv('naive_data.csv')

    # Create the plot
    # plt.figure(figsize=(8, 6))  # Optional: set figure size

    # Plot "Time" data on the primary y-axis with a blue color and circle markers
    # naive_df.plot(x="matrix_size", y=["time", "L1_DCM", "L2_DCM"], marker='o', color=['blue', 'red', 'green'])
    plt.plot(naive_df["matrix_size"], naive_df["time"], label="Time (s)", marker='o', color='blue')

    # Create a secondary y-axis and plot "L1 DCM" data on it with a red color and circle markers
    ax2 = plt.twinx()
    ax2.plot(naive_df["matrix_size"], naive_df["L1_DCM"], label="L1 DCM", marker='o', color='red')
    ax2.plot(naive_df["matrix_size"], naive_df["L2_DCM"], label="L2 DCM", marker='o', color='green')

    # # Create another secondary y-axis and plot "L2 DCM" data on it with a green color and circle markers
    # ax3 = plt.twinx()
    # ax3.plot(naive_df["matrix_size"], naive_df["L2_DCM"], label="L2 DCM", marker='o', color='green')

    # # Set labels and title
    plt.xlabel("Matrix Size")
    plt.ylabel("Time (s)", color='blue')  # Label for primary y-axis
    ax2.set_ylabel("DCM", color='red')  # Label for secondary y-axis (L1/L2 DCM)
    # ax2.set_ylabel("L1 DCM", color='red')  # Label for secondary y-axis (L1 DCM)
    # ax3.set_ylabel("L2 DCM", color='green')  # Label for secondary y-axis (L2 DCM)
    # plt.title("Time vs L1/L2 DCM (Naive Matrix Multiplication)")

    # # Adjust y-axis limits for better visualization (optional)
    # # plt.ylim1(bottom=0)  # Set minimum for primary y-axis (optional)
    # # ax2.set_ylim(bottom=0)  # Set minimum for secondary y-axis (L1 DCM) (optional)
    # ax3.set_ylim(bottom=0)  # Set minimum for secondary y-axis (L2 DCM) (optional)

    # Add legend
    plt.legend()

    # Show the plot
    plt.grid(True)

    plt.savefig("plot_L1_L2_vs_time.png")
    plt.clf()



def plot_go_vs_cpp():
    naive_df_cpp = pd.read_csv('naive_data.csv')
    naive_df_go = pd.read_csv('naive_data_go.csv')
    line_df_cpp = pd.read_csv('line_data.csv')
    line_df_go = pd.read_csv('line_data_go.csv')
    plot_line(naive_df_cpp, label="Naive C++")
    plot_line(naive_df_go, label="Naive Go")
    plot_line(line_df_cpp, label="Line C++")
    plot_line(line_df_go, label="Line Go")

    finish_plot("Go vs C++ Matrix Multiplication")

def plot_naive_go_vs_cpp():
    naive_df_cpp = pd.read_csv('naive_data.csv')
    naive_df_go = pd.read_csv('naive_data_go.csv')
    plot_line(naive_df_cpp, label="Naive C++")
    plot_line(naive_df_go, label="Naive Go")

    finish_plot("Naive Matrix Multiplication: Go vs C++")

def plot_line_go_vs_cpp():
    line_df_cpp = pd.read_csv('line_data.csv')
    line_df_go = pd.read_csv('line_data_go.csv')
    plot_line(line_df_cpp, label="Line C++")
    plot_line(line_df_go, label="Line Go")

    finish_plot("Line Matrix Multiplication: Go vs C++")


for variable in ["time", "L1_DCM", "L2_DCM"]:
    plot_line_vs_block(variable)
    plot_naive_vs_line(variable)

plot_L1_L2_vs_time()
plot_naive_go_vs_cpp()
plot_line_go_vs_cpp()
plot_go_vs_cpp()
