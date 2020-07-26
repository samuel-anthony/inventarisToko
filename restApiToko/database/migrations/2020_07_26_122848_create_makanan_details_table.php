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
            $table->unsignedBigInteger('bahan_pokok_id');
            $table->unsignedBigInteger('supplier_id')->nullable();
            $table->foreign('bahan_pokok_id')->references('bahan_pokok_id')->on('bahan_pokoks');
            $table->foreign('supplier_id')->references('supplier_id')->on('supplier');
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
