<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateMakananDetailsTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('makanan_details', function (Blueprint $table) {
            $table->bigIncrements('makanan_detail_id');
            $table->integer('jumlah');
            $table->unsignedBigInteger('makanan_id');
            $table->foreign('makanan_id')->references('makanan_id')->on('makanans');
            $table->unsignedBigInteger('bahan_pokok_id');
            $table->foreign('bahan_pokok_id')->references('bahan_pokok_id')->on('bahan_pokoks');
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('makanan_details');
    }
}
